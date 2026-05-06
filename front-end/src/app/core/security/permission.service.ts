import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class PermissionService {

    private permissions = new Set<string>();
    private roles = new Set<string>();

    // ========================
    // INIT (after login)
    // ========================
    setPermissions(perms: string[]) {
        this.permissions = new Set(
            perms.map(p => p.toUpperCase())
        );
    }

    setRoles(roles: string[]) {
        this.roles = new Set(roles || []);
    }

    // ========================
    // CHECKS
    // ========================
    has(resource: string, action: string): boolean {

        const permission =
            `${resource}:${action}`.toUpperCase();

        console.log('CHECKING=', permission);
        console.log('AVAILABLE=', [...this.permissions]);

        return this.permissions.has(permission);
    }

    hasRaw(permission: string): boolean {
        return this.permissions.has(permission.toUpperCase());
    }

    hasAny(perms: string[]): boolean {
        return perms.some(p => this.permissions.has(p));
    }

    hasAll(perms: string[]): boolean {
        return perms.every(p => this.permissions.has(p));
    }

    getAll(): Set<string> {
        return this.permissions;
    }
}
