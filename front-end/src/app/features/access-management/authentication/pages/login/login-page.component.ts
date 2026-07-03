import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { RippleModule } from 'primeng/ripple';

import { AuthService } from '@features/access-management/authentication/services/auth.service';
import { AuthTenantService } from '@services/auth/auth-tenant.service';
import { AppToastService } from '@services/toast/app-toast.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [ButtonModule, CheckboxModule, InputTextModule, PasswordModule, FormsModule, RouterModule, RippleModule],
    templateUrl: './login-page.component.html',
    styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent {
    username = '';
    password = '';
    checked = false;
    loggingIn = false;
    showPassword = false;

    private authService = inject(AuthService);
    private router = inject(Router);
    private route = inject(ActivatedRoute);
    private authTenantService = inject(AuthTenantService);
    private toast = inject(AppToastService);

    private returnUrl = '/dashboard';
    private tenantCode = 'QR';

    ngOnInit() {
        // ✅ Get returnUrl or fallback to dashboard
        this.returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || '/dashboard';
    }

    login() {
        if (!this.tenantCode || !this.username || !this.password || this.loggingIn) {
            return;
        }

        // =========================
        // SET TENANT BEFORE LOGIN
        // =========================
        this.authTenantService.setTenant('', this.tenantCode);

        this.loggingIn = true;

        this.authService.login(this.username, this.password).subscribe({
            next: (response) => {
                // ✅ Only tenant here (token already handled in service)
                // (keep this only if required in your system)
                if (response?.tenant_code) {
                    // if still needed, inject tokenService back
                    // otherwise remove completely
                }

                // ✅ Correct navigation (Promise handled)
                void this.router.navigateByUrl(this.returnUrl);
            },
            error: (err: unknown) => {
                const message = err instanceof Error ? err.message : 'Login failed';

                this.toast.error(message);

                this.loggingIn = false;
            }
        });
    }
}
