import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '@features/administration/access-management/authentication/services/auth.service';
import { AppToastService } from '@services/toast/app-toast.service';

@Component({
    selector: 'app-oidc-callback',
    standalone: true,
    template: `<div class="min-h-screen flex items-center justify-center text-sm text-gray-600">Signing you in...</div>`
})
export class OidcCallbackComponent {
    private route = inject(ActivatedRoute);
    private router = inject(Router);
    private authService = inject(AuthService);
    private toast = inject(AppToastService);

    ngOnInit() {
        const code = this.route.snapshot.queryParamMap.get('code');
        const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || '/dashboard';

        if (!code) {
            this.toast.error('OIDC login failed');
            void this.router.navigate(['/auth/login']);
            return;
        }

        this.authService.exchangeOidcCode(code).subscribe({
            next: () => void this.router.navigateByUrl(returnUrl),
            error: () => {
                this.toast.error('OIDC login failed');
                void this.router.navigate(['/auth/login']);
            }
        });
    }
}

