import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { RippleModule } from 'primeng/ripple';
import { SelectModule } from 'primeng/select';

import { AuthService } from '@features/administration/access-management/authentication/services/auth.service';
import { AuthSource, IdentityProviderConfig, TenantAuthContext } from '@features/tenant-administration/models/tenant.model';
import { AuthTenantService } from '@services/auth/auth-tenant.service';
import { AppToastService } from '@services/toast/app-toast.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, ButtonModule, CheckboxModule, InputTextModule, PasswordModule, FormsModule, RouterModule, RippleModule, SelectModule],
    templateUrl: './login-page.component.html',
    styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent {
    username = '';
    password = '';
    checked = false;
    loggingIn = false;
    loadingContext = false;
    showPassword = false;
    tenantCode = 'QR';
    authContext?: TenantAuthContext;
    availableProviders: { label: string; value: AuthSource }[] = [];
    selectedAuthSource: AuthSource = AuthSource.INTERNAL;

    private authService = inject(AuthService);
    private router = inject(Router);
    private route = inject(ActivatedRoute);
    private authTenantService = inject(AuthTenantService);
    private toast = inject(AppToastService);

    private returnUrl = '/dashboard';

    ngOnInit() {
        this.returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || '/dashboard';
        this.tenantCode = this.authTenantService.getTenantCode() || 'QR';
        void this.resolveTenantContext();
    }

    login() {
        if (!this.tenantCode || this.loggingIn) {
            return;
        }

        this.authTenantService.setTenant('', this.tenantCode.trim().toUpperCase());
        this.loggingIn = true;

        if (this.isOidcSelected()) {
            this.authService.startOidcLogin(this.tenantCode, this.selectedAuthSource);
            this.loggingIn = false;
            return;
        }

        if (!this.username || !this.password) {
            this.loggingIn = false;
            return;
        }

        this.authService.login(this.username, this.password, this.selectedAuthSource).subscribe({
            next: () => {
                void this.router.navigateByUrl(this.returnUrl);
            },
            error: (err: unknown) => {
                const message = err instanceof Error ? err.message : 'Login failed';
                this.toast.error(message);
                this.loggingIn = false;
            }
        });
    }

    resolveTenantContext() {
        const tenantCode = this.tenantCode?.trim().toUpperCase();
        if (!tenantCode) {
            return Promise.resolve();
        }

        this.loadingContext = true;
        return new Promise<void>((resolve) => {
            this.authService.getTenantAuthContext(tenantCode).subscribe({
                next: (context) => {
                    this.authContext = context;
                    this.availableProviders = (context.identityProviders || []).map((provider: IdentityProviderConfig) => ({
                        label: this.providerLabel(provider.authSource),
                        value: provider.authSource
                    }));
                    this.selectedAuthSource = this.availableProviders[0]?.value || AuthSource.INTERNAL;
                    this.loadingContext = false;
                    resolve();
                },
                error: () => {
                    this.authContext = undefined;
                    this.availableProviders = [{ label: 'Internal', value: AuthSource.INTERNAL }];
                    this.selectedAuthSource = AuthSource.INTERNAL;
                    this.loadingContext = false;
                    resolve();
                }
            });
        });
    }

    isOidcSelected(): boolean {
        return [AuthSource.AZURE, AuthSource.KEYCLOAK, AuthSource.OKTA, AuthSource.OIDC_GENERIC].includes(this.selectedAuthSource);
    }

    private providerLabel(authSource: AuthSource): string {
        switch (authSource) {
            case AuthSource.INTERNAL:
                return 'Internal';
            case AuthSource.LDAP:
                return 'LDAP';
            case AuthSource.AZURE:
                return 'Azure AD';
            case AuthSource.KEYCLOAK:
                return 'Keycloak';
            case AuthSource.OKTA:
                return 'Okta';
            case AuthSource.OIDC_GENERIC:
                return 'OpenID Connect';
            default:
                return authSource;
        }
    }
}

