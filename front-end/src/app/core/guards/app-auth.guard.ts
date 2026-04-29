import { Injectable, inject } from '@angular/core';
import {
    CanActivate,
    CanActivateChild,
    Router,
    ActivatedRouteSnapshot,
    RouterStateSnapshot
} from '@angular/router';
import { AppTokenService } from '@services/auth/app-token.service';

@Injectable({ providedIn: 'root' })
export class AppAuthGuard implements CanActivate, CanActivateChild {

    private tokenService = inject(AppTokenService);
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

        const userRoles = this.tokenService.roles;
        const userPermissions = this.tokenService.permissions;

        const hasRole =
            !requiredRoles || requiredRoles.length === 0 ||
            requiredRoles.some(r => userRoles.includes(r));

        const hasPermission =
            !requiredPermissions || requiredPermissions.length === 0 ||
            requiredPermissions.some(p => userPermissions.includes(p));

        if (!hasRole || !hasPermission) {
            this.router.navigate(['/access-denied']); // keep path consistent
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
