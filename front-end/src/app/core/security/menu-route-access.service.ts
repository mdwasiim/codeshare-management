import { Injectable } from '@angular/core';
import { AppMenuModel } from '@features/access-management/models/app-menu.model';

@Injectable({
    providedIn: 'root'
})
export class MenuRouteAccessService {
    private readonly publicPrefixes = ['/dashboard', '/unauthorized', '/access-denied', '/error', '/notfound'];

    private readonly routeAliases: Record<string, string[]> = {
        '/tenants': ['/tenants/:id', '/tenants/:id/edit', '/tenants/:id/overview', '/tenants/:id/identity', '/tenants/:id/ingestion', '/tenants/:id/partners'],
        '/tenant-identity-providers': ['/tenant-onboarding/:id/identity-providers', '/tenants/:id/identity'],
        '/tenant-oidc-config': ['/tenant-onboarding/:id/oidc-config'],
        '/tenant-ingestion-profiles': ['/tenant-onboarding/:id/ingestion-profiles', '/tenants/:id/ingestion'],
        '/tenant-ingestion-channels': ['/tenant-onboarding/:id/ingestion-channels'],
        '/tenant-codeshare-partners': ['/tenant-onboarding/:id/codeshare-partners', '/tenants/:id/partners', '/tenant-partners', '/tenant-partners/create', '/tenant-partners/:id'],
        '/tenant-partner-profiles': ['/tenant-onboarding/:id/partner-profiles'],
        '/tenant-communication-profiles': ['/tenant-onboarding/:id/communication-profiles'],
        '/tenant-distribution-profiles': ['/tenant-onboarding/:id/distribution-profiles']
    };

    isPublicUrl(url: string): boolean {
        const normalized = this.normalizeUrl(url);
        return this.publicPrefixes.some((prefix) => this.matchesPrefix(normalized, prefix));
    }

    findBestMatch(url: string, menus: AppMenuModel[]): AppMenuModel | null {
        const normalized = this.normalizeUrl(url);
        const candidates = this.flatten(menus)
            .filter((menu) => !!menu.route)
            .filter((menu) => this.matchesMenuRoute(normalized, menu.route!))
            .sort((a, b) => this.getSpecificityScore(b.route!) - this.getSpecificityScore(a.route!));

        return candidates[0] ?? null;
    }

    matchesMenuRoute(url: string, route: string): boolean {
        const normalizedUrl = this.normalizeUrl(url);
        const patterns = [route, ...(this.routeAliases[route] ?? [])];
        return patterns.some((pattern) => this.matchesPattern(normalizedUrl, pattern));
    }

    private flatten(nodes: AppMenuModel[]): AppMenuModel[] {
        return nodes.flatMap((node) => [node, ...this.flatten(node.items ?? [])]);
    }

    private normalizeUrl(url: string): string {
        const [pathname] = (url || '').split(/[?#]/, 1);
        if (!pathname) {
            return '/';
        }

        const normalized = pathname.startsWith('/') ? pathname : `/${pathname}`;
        return normalized.length > 1 ? normalized.replace(/\/+$/, '') : normalized;
    }

    private matchesPattern(url: string, pattern: string): boolean {
        if (!pattern) {
            return false;
        }

        if (!pattern.includes(':')) {
            return this.matchesPrefix(url, pattern);
        }

        const escaped = pattern
            .replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
            .replace(/:([A-Za-z0-9_]+)/g, '[^/]+');

        return new RegExp(`^${escaped}(?:/|$)`).test(url);
    }

    private matchesPrefix(url: string, prefix: string): boolean {
        return url === prefix || url.startsWith(`${prefix}/`);
    }

    private getSpecificityScore(route: string): number {
        return route.split('/').filter(Boolean).length * 100 + route.length;
    }
}
