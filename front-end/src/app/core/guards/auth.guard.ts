import { Injectable, inject } from '@angular/core';
import {
  CanActivate,
  CanActivateChild,
  Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot
} from '@angular/router';
import { TokenService } from '@services/auth/token.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate, CanActivateChild {

  private tokenService = inject(TokenService);
  private router = inject(Router);

  private checkAuth(state: RouterStateSnapshot): boolean {

    // ✅ MUST check validity, not just existence
    if (this.tokenService.isAuthenticated()) {
      return true;
    }

    // ❌ Not authenticated → redirect to login
    this.router.navigate(['/auth/login'], {
      queryParams: { returnUrl: state.url }
    });

    return false;
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    return this.checkAuth(state);
  }

  canActivateChild(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    return this.checkAuth(state);
  }
}
