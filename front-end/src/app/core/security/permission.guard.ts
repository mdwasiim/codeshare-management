import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { PermissionService } from '@core/security/permission.service';

@Injectable({ providedIn: 'root' })
export class PermissionGuard implements CanActivate {

    constructor(
        private permissionService: PermissionService,
        private router: Router
    ) {}

    canActivate(route: ActivatedRouteSnapshot): boolean {

        const permissions = route.data?.['permissions'] as string[];

        if (!permissions || permissions.length === 0) return true;

        const hasAccess = this.permissionService.hasAny(permissions);

        if (!hasAccess) {
            this.router.navigate(['/unauthorized']);
            return false;
        }

        return true;
    }
}
