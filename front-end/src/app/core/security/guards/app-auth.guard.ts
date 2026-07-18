import { Injectable, inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

import { MenuRouteAccessService } from '@core/security/menu-route-access.service';
import { PermissionService } from '@core/security/permission.service';
import { LayoutMenuService } from '@layout/services/layout-menu.service';
import { AuthReturnUrlService } from '@services/auth/auth-return-url.service';
import { AuthService } from '@services/auth/auth.service';
import { AuthTokenService } from '@services/auth/auth-token.service';

@Injectable({ providedIn: 'root' })
export class AppAuthGuard implements CanActivate, CanActivateChild {
    private tokenService = inject(AuthTokenService);
    private router = inject(Router);
    private authService = inject(AuthService);
    private returnUrlService = inject(AuthReturnUrlService);
    private permissionService = inject(PermissionService);
    private menuService = inject(LayoutMenuService);
    private menuRouteAccessService = inject(MenuRouteAccessService);

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | UrlTree {
        return this.checkAccess(route, state);
    }

    canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | UrlTree {
        return this.checkAccess(route, state);
    }

    private checkAccess(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | UrlTree {
        if (!this.tokenService.isAuthenticated()) {
            this.returnUrlService.remember(state.url);
            return this.router.createUrlTree(['/login']);
        }

        return this.ensureAccessLoaded().pipe(
            map(() => this.evaluateAccess(route, state)),
            catchError(() => of(this.buildUnauthorizedTree(state.url)))
        );
    }

    private ensureAccessLoaded(): Observable<unknown> {
        const loadSession$: Observable<unknown> =
            this.tokenService.roles.length || this.tokenService.permissions.length || this.tokenService.groups.length
                ? of(null)
                : this.authService.loadSession();

        return loadSession$.pipe(
            switchMap(() => (this.menuService.hasLoadedMenus() ? of(this.menuService.getMenuSnapshot()) : this.menuService.loadMenus()))
        );
    }

    private evaluateAccess(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree {
        const requiredRoles = (route.data?.['roles'] as string[] | undefined) ?? [];
        const requiredPermissions = (route.data?.['permissions'] as string[] | undefined) ?? [];

        if (requiredRoles.length && !this.permissionService.hasAnyRole(requiredRoles)) {
            return this.buildUnauthorizedTree(state.url);
        }

        if (requiredPermissions.length && !this.permissionService.hasAnyPermission(requiredPermissions)) {
            return this.buildUnauthorizedTree(state.url);
        }

        if (this.menuRouteAccessService.isPublicUrl(state.url)) {
            return true;
        }

        const matchedMenu = this.menuRouteAccessService.findBestMatch(state.url, this.menuService.getMenuSnapshot());
        if (!matchedMenu) {
            return this.buildUnauthorizedTree(state.url);
        }

        if (matchedMenu.permissionCode && !this.permissionService.hasRawPermission(matchedMenu.permissionCode)) {
            return this.buildUnauthorizedTree(state.url);
        }

        return true;
    }

    private buildUnauthorizedTree(returnUrl: string): UrlTree {
        return this.router.createUrlTree(['/unauthorized'], {
            queryParams: { returnUrl }
        });
    }
}

