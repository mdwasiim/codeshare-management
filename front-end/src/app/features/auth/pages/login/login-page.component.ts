import {Component, inject} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';

import {ButtonModule} from 'primeng/button';
import {CheckboxModule} from 'primeng/checkbox';
import {InputTextModule} from 'primeng/inputtext';
import {PasswordModule} from 'primeng/password';
import {RippleModule} from 'primeng/ripple';


import {AuthService} from '@features/auth/services/auth.service';
import {AppTokenService} from '@services/auth/app-token.service';

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
    RippleModule
  ],
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent {

  username = '';
  password = '';
  checked = false;
  loggingIn = false;
  showPassword: boolean = false;

  private authService = inject(AuthService);
  private tokenService = inject(AppTokenService);
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
      this.tokenService.setSession(
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
