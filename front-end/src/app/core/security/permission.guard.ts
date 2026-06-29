import { Injectable } from '@angular/core';

import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';

import { PermissionService } from '@core/security/permission.service';

@Injectable({
    providedIn: 'root'
})
export class PermissionGuard implements CanActivate {
    constructor(
        private permissionService: PermissionService,
        private router: Router
    ) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        const permissions = route.data?.['permissions'] as string[] | undefined;

        // no permission required
        if (!permissions?.length) {
            return true;
        }

        const hasAccess = this.permissionService.hasAnyPermission(permissions);

        if (hasAccess) {
            return true;
        }

        this.router.navigate(['/unauthorized'], {
            queryParams: {
                returnUrl: state.url
            }
        });

        return false;
    }
}
