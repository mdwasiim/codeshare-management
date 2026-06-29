import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class PermissionService {
    private permissions = new Set<string>();

    private roles = new Set<string>();

    private groups = new Set<string>();

    // =========================
    // INITIALIZATION
    // =========================
    setPermissions(permissions: string[] = []): void {
        this.permissions = new Set(permissions.map((p) => p.toUpperCase()));
    }

    setRoles(roles: string[] = []): void {
        this.roles = new Set(roles.map((r) => r.toUpperCase()));
    }

    setGroups(groups: string[] = []): void {
        this.groups = new Set(groups.map((g) => g.toUpperCase()));
    }

    clear(): void {
        this.permissions.clear();
        this.roles.clear();
        this.groups.clear();
    }

    // =========================
    // PERMISSION CHECKS
    // =========================
    hasPermission(resource: string, action: string): boolean {
        return this.hasRawPermission(`${resource}:${action}`);
    }

    hasRawPermission(permission: string): boolean {
        const normalized = permission.toUpperCase();

        // direct permission
        if (this.permissions.has(normalized)) {
            return true;
        }

        // wildcard support
        const [resource] = normalized.split(':');

        return this.permissions.has(`${resource}:*`);
    }

    hasAnyPermission(permissions: string[]): boolean {
        return permissions.some((permission) => this.hasRawPermission(permission));
    }

    hasAllPermissions(permissions: string[]): boolean {
        return permissions.every((permission) => this.hasRawPermission(permission));
    }

    // =========================
    // ROLE CHECKS
    // =========================
    hasRole(role: string): boolean {
        return this.roles.has(role.toUpperCase());
    }

    hasAnyRole(roles: string[]): boolean {
        return roles.some((role) => this.hasRole(role));
    }

    // =========================
    // GROUP CHECKS
    // =========================
    hasGroup(group: string): boolean {
        return this.groups.has(group.toUpperCase());
    }

    hasAnyGroup(groups: string[]): boolean {
        return groups.some((group) => this.hasGroup(group));
    }

    // =========================
    // GETTERS
    // =========================
    getPermissions(): string[] {
        return [...this.permissions];
    }

    getRoles(): string[] {
        return [...this.roles];
    }

    getGroups(): string[] {
        return [...this.groups];
    }

    setUserAccess(roles: string[] = [], permissions: string[] = [], groups: string[] = []): void {
        this.setRoles(roles);
        this.setPermissions(permissions);
        this.setGroups(groups);
    }
}
