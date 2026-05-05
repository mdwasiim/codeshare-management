import { Injectable } from '@angular/core';
import {AppTokenService} from "@services/auth/app-token.service";

@Injectable({ providedIn: 'root' })
export class AuthzService {

    private userRoles: Set<string> = new Set();
    private userPermissions: Set<string> = new Set();

    constructor(private tokenService: AppTokenService) {}


    setUserAccess(roles: string[], permissions: string[]) {
        this.userRoles = new Set((roles || this.tokenService.roles || [])
            .map(r => r.toUpperCase()));

        this.userPermissions = new Set(
            (permissions || this.tokenService.permissions || [])
                .map(p => p.toUpperCase())
        );
    }

    clear() {
        this.userRoles.clear();
        this.userPermissions.clear();
    }

    // =========================
    // NEW METHODS (IMPORTANT)
    // =========================

    has(resource: string, action: string): boolean {
        const key = `${resource}:${action}`.toUpperCase();
        return this.userPermissions.has(key);
    }

    hasRaw(permission: string): boolean {
        return this.userPermissions.has(permission.toUpperCase());
    }

    hasAny(perms?: string[]): boolean {
        if (!perms || perms.length === 0) return true;
        return perms.some(p => this.userPermissions.has(p.toUpperCase()));
    }

    hasRole(roles?: string[]): boolean {
        if (!roles || roles.length === 0) return true;
        return roles.some(role => this.userRoles.has(role.toUpperCase()));
    }

    // optional
    getAllPermissions(): Set<string> {
        return this.userPermissions;
    }
}
