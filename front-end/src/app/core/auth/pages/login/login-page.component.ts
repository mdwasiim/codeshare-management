import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { RippleModule } from 'primeng/ripple';
import { SelectModule } from 'primeng/select';

import { AuthSource, IdentityProviderConfig, TenantAuthContext, TenantLoginOption } from '@features/tenant-administration/models/tenant.model';
import { AuthReturnUrlService } from '@services/auth/auth-return-url.service';
import { AuthService } from '@services/auth/auth.service';
import { AuthTenantService } from '@services/auth/auth-tenant.service';
import { AppToastService } from '@services/toast/app-toast.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, ButtonModule, InputTextModule, PasswordModule, FormsModule, RouterModule, RippleModule, SelectModule],
    templateUrl: './login-page.component.html',
    styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent {
    username = '';
    password = '';
    loggingIn = false;
    loadingContext = false;
    loadingTenants = false;
    showPassword = false;
    tenantCode = '';
    authContext?: TenantAuthContext;
    availableTenants: { label: string; value: string }[] = [];
    showCredentialStep = false;
    selectedAuthSource: AuthSource = AuthSource.INTERNAL;

    private authService = inject(AuthService);
    private router = inject(Router);
    private route = inject(ActivatedRoute);
    private authTenantService = inject(AuthTenantService);
    private returnUrlService = inject(AuthReturnUrlService);
    private toast = inject(AppToastService);

    ngOnInit() {
        const routeTenantCode =
            this.route.snapshot.queryParamMap.get('tenant')
            || this.route.snapshot.queryParamMap.get('tenantCode');

        this.tenantCode = this.normalizeTenantCode(routeTenantCode || this.authTenantService.getTenantCode() || '');
        void this.loadTenantOptions();
    }

    async login() {
        if (this.showCredentialStep) {
            this.submitCredentials();
            return;
        }

        await this.continueWithTenant();
    }

    onTenantChange(): void {
        this.showCredentialStep = false;
        this.username = '';
        this.password = '';
        this.showPassword = false;
        this.authContext = undefined;
        this.selectedAuthSource = AuthSource.INTERNAL;
    }

    changeTenant(): void {
        this.showCredentialStep = false;
        this.username = '';
        this.password = '';
        this.showPassword = false;
    }

    canSubmit(): boolean {
        if (this.loggingIn || this.loadingContext || this.loadingTenants) {
            return false;
        }

        if (this.showCredentialStep) {
            return !!this.username && !!this.password;
        }

        return !!this.tenantCode;
    }

    primaryActionLabel(): string {
        if (this.loadingTenants || this.loadingContext) {
            return 'Loading...';
        }

        return this.showCredentialStep ? 'Sign In' : 'Continue';
    }

    selectedTenantLabel(): string {
        return this.authContext?.name || this.tenantCode;
    }

    private async loadTenantOptions(): Promise<void> {
        this.loadingTenants = true;

        try {
            const tenants = await firstValueFrom(this.authService.getTenantLoginOptions());
            this.availableTenants = tenants.map(this.toTenantOption);

            if (this.tenantCode) {
                const resolved = await this.resolveTenantContext(false);
                if (resolved && !this.isOidcSelected()) {
                    this.showCredentialStep = true;
                }
            }
        } catch {
            this.availableTenants = [];
            if (this.tenantCode) {
                const resolved = await this.resolveTenantContext(false);
                if (resolved && !this.isOidcSelected()) {
                    this.showCredentialStep = true;
                }
            } else {
                this.toast.error('Failed to load tenant options');
            }
        } finally {
            this.loadingTenants = false;
        }
    }

    private async continueWithTenant(): Promise<void> {
        const resolved = await this.resolveTenantContext();
        if (!resolved) {
            return;
        }

        this.authTenantService.setTenant('', this.tenantCode);

        if (this.isOidcSelected()) {
            this.loggingIn = true;
            this.authService.startOidcLogin(this.tenantCode, this.selectedAuthSource);
            return;
        }

        this.showCredentialStep = true;
    }

    private submitCredentials(): void {
        if (!this.tenantCode || !this.username || !this.password || this.loggingIn) {
            return;
        }

        this.authTenantService.setTenant('', this.tenantCode);
        this.loggingIn = true;

        this.authService.login(this.username, this.password, this.selectedAuthSource).subscribe({
            next: () => {
                void this.router.navigateByUrl(this.returnUrlService.consume());
            },
            error: (err: unknown) => {
                const message = err instanceof Error ? err.message : 'Login failed';
                this.toast.error(message);
                this.loggingIn = false;
            }
        });
    }

    private async resolveTenantContext(showError = true): Promise<boolean> {
        const tenantCode = this.normalizeTenantCode(this.tenantCode);
        if (!tenantCode) {
            if (showError) {
                this.toast.error('Select a tenant first');
            }
            return false;
        }

        this.loadingContext = true;
        try {
            const context = await firstValueFrom(this.authService.getTenantAuthContext(tenantCode));
            const primaryProvider = (context.identityProviders || []).find(
                (provider: IdentityProviderConfig): provider is IdentityProviderConfig & { authSource: AuthSource } => !!provider.authSource
            );

            if (!primaryProvider?.authSource) {
                throw new Error('No enabled authentication provider configured for this tenant');
            }

            this.tenantCode = tenantCode;
            this.authContext = context;
            this.selectedAuthSource = primaryProvider.authSource;
            return true;
        } catch (error) {
            this.authContext = undefined;
            this.selectedAuthSource = AuthSource.INTERNAL;
            if (showError) {
                const message = error instanceof Error ? error.message : 'Unable to load tenant authentication settings';
                this.toast.error(message);
            }
            return false;
        } finally {
            this.loadingContext = false;
        }
    }

    private isOidcSelected(): boolean {
        return [AuthSource.AZURE, AuthSource.KEYCLOAK, AuthSource.OKTA, AuthSource.OIDC_GENERIC].includes(this.selectedAuthSource);
    }

    private normalizeTenantCode(value: string): string {
        return value.trim().toUpperCase();
    }

    private toTenantOption(tenant: TenantLoginOption): { label: string; value: string } {
        return {
            label: `${tenant.name} (${tenant.tenantCode})`,
            value: tenant.tenantCode
        };
    }
}
