import { Injectable } from '@angular/core';
import {AppTokenService} from "@services/auth/app-token.service";

@Injectable({ providedIn: 'root' })
export class AuthzService {

    private userRoles: string[] = [];
    private userPermissions: string[] = [];

    constructor(private tokenService: AppTokenService) {}
    /**
     * Call this after login (from token or API)
     */
    setUserAccess(roles: string[], permissions: string[]) {
        this.userRoles = roles?.length ? roles : this.tokenService.roles;
        this.userPermissions = permissions?.length ? permissions : this.tokenService.permissions;
    }

    /**
     * Clear on logout
     */
    clear() {
        this.userRoles = [];
        this.userPermissions = [];
    }

    hasRole(roles?: string[]): boolean {
        if (!roles || roles.length === 0) return true;
        return roles.some(role => this.userRoles.includes(role));
    }

    hasPermission(perms?: string[]): boolean {
        if (!perms || perms.length === 0) return true;
        return perms.some(p => this.userPermissions.includes(p));
    }
}
