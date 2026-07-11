import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

import { AuthReturnUrlService } from '@services/auth/auth-return-url.service';
import { AuthService } from '@services/auth/auth.service';
import { AppToastService } from '@services/toast/app-toast.service';

@Component({
    selector: 'app-oidc-callback',
    standalone: true,
    template: `<div class="min-h-screen flex items-center justify-center text-sm text-gray-600">Signing you in...</div>`
})
export class OidcCallbackComponent {
    private router = inject(Router);
    private authService = inject(AuthService);
    private returnUrlService = inject(AuthReturnUrlService);
    private toast = inject(AppToastService);

    ngOnInit() {
        const searchParams = new URLSearchParams(window.location.search);
        const code = searchParams.get('code');

        if (!code) {
            this.toast.error('OIDC login failed');
            void this.router.navigate(['/login']);
            return;
        }

        this.authService.exchangeOidcCode(code).subscribe({
            next: () => void this.router.navigateByUrl(this.returnUrlService.consume()),
            error: () => {
                this.toast.error('OIDC login failed');
                void this.router.navigate(['/login']);
            }
        });
    }
}
