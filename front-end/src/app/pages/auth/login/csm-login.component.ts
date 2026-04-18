import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';

import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { RippleModule } from 'primeng/ripple';

import { CSMFloatingConfigurator } from '@/layout/floating-configurator/csm.floating-configurator';

import { AuthService } from '@services/auth/auth.service';
import { TokenService } from '@services/auth/token.service';
import { CSMMenuService } from '@/layout/menu/service/csm-menu.service';

@Component({
  selector: 'csm-login',
  standalone: true,
  imports: [
    ButtonModule,
    CheckboxModule,
    InputTextModule,
    PasswordModule,
    FormsModule,
    RouterModule,
    RippleModule,
    CSMFloatingConfigurator
  ],
  templateUrl: './csm-login.component.html',
  styleUrls: ['./csm-login.component.scss']
})
export class CSMLogin {

  username = '';
  password = '';
  checked = false;
  loggingIn = false;

  private authService = inject(AuthService);
  private tokenService = inject(TokenService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  private returnUrl = '/dashboard';

  ngOnInit() {
    this.returnUrl =
      this.route.snapshot.queryParamMap.get('returnUrl') || '/dashboard';
  }

login() {
  if (!this.username || !this.password || this.loggingIn) {
    return;
  }

  this.loggingIn = true;

  this.authService.login(this.username, this.password).subscribe({
    next: (response) => {

      console.log('🚀 LOGOUT TOKEN:', this.tokenService.refreshToken);

      // ✅ Store tokens
      this.tokenService.setTokens(
        response.access_token,
        response.refresh_token,
        response.expires_in
      );

      // ✅ Store tenant (important for backend)
      this.tokenService.setTenant(response.tenant_code);

      // ✅ Navigate
      this.router.navigate([this.returnUrl]);
    },
    error: (err: unknown) => {

      console.error('Login failed:', err);

      const message =
        err instanceof Error
          ? err.message
          : 'Login failed';

      alert(message);

      this.loggingIn = false;
    }
  });
}


}
