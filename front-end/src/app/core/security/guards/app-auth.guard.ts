import { Injectable, inject } from '@angular/core';
import { CanActivate, CanActivateChild, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthTokenService } from '@services/auth/auth-token.service';

@Injectable({ providedIn: 'root' })
export class AppAuthGuard implements CanActivate, CanActivateChild {
    private tokenService = inject(AuthTokenService);
    private router = inject(Router);

    private checkAccess(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        // 🔐 1. Authentication
        if (!this.tokenService.isAuthenticated()) {
            this.router.navigate(['/auth/login'], {
                queryParams: { returnUrl: state.url }
            });
            return false;
        }

        // 🔐 2. RBAC (optional per route)
        const requiredRoles = route.data?.['roles'] as string[] | undefined;
        const requiredPermissions = route.data?.['permissions'] as string[] | undefined;

        const userRoles = this.tokenService.roles.map((r) => r.toUpperCase());
        const userPermissions = this.tokenService.permissions.map((p) => p.toUpperCase());
        const normalizedRequiredRoles = requiredRoles?.map((r) => r.toUpperCase());
        const normalizedRequiredPermissions = requiredPermissions?.map((p) => p.toUpperCase());

        const hasRole = !normalizedRequiredRoles || normalizedRequiredRoles.length === 0 || normalizedRequiredRoles.some((r) => userRoles.includes(r));

        const hasPermission = !normalizedRequiredPermissions || normalizedRequiredPermissions.length === 0 || normalizedRequiredPermissions.some((p) => userPermissions.includes(p));

        if (!hasRole || !hasPermission) {
            this.router.navigate(['/unauthorized'], {
                queryParams: { returnUrl: state.url }
            });
            return false;
        }

        return true;
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        return this.checkAccess(route, state);
    }

    canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        return this.checkAccess(route, state);
    }
}
