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
  if (!this.username || !this.password) {
    return;
  }

  this.authService.login(this.username, this.password).subscribe({
    next: (response) => {

      // ✅ CORRECT field names (snake_case)
      this.tokenService.setTokens(
        response.access_token,
        response.refresh_token,
        response.expires_in
      );

      // ✅ Navigate AFTER token is stored
      this.router.navigateByUrl(this.returnUrl);
    },
    error: (err) => {
      console.error('Login failed', err);
    }
  });
}

}
