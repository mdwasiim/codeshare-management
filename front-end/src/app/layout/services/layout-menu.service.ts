import { Injectable } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { filter, map, shareReplay, tap } from 'rxjs/operators';

import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { MenuRouteAccessService } from '@core/security/menu-route-access.service';
import { AppMenuModel } from '@features/administration/access-management/models/app-menu.model';

type MenuApiItem = Partial<AppMenuModel> & {
    id?: string;
    code?: string;
    label?: string;
    topbarLabel?: string | null;
    sidebarLabel?: string | null;
    parentId?: string | null;
    parentCode?: string | null;
    visible?: boolean | null;
};

@Injectable({
    providedIn: 'root'
})
export class LayoutMenuService {
    private sidebarMenuSubject = new BehaviorSubject<AppMenuModel[]>([]);
    sidebarMenu$ = this.sidebarMenuSubject.asObservable();

    private menuSubject = new BehaviorSubject<AppMenuModel[]>([]);
    private menu$ = this.menuSubject.asObservable();
    private loadMenusRequest$?: Observable<AppMenuModel[]>;

    private selectedRootMenuSubject = new BehaviorSubject<AppMenuModel | null>(null);
    selectedRootMenu$ = this.selectedRootMenuSubject.asObservable();

    constructor(
        private apiService: AppApiService,
        private router: Router,
        private menuRouteAccessService: MenuRouteAccessService
    ) {
        this.router.events.pipe(filter((event) => event instanceof NavigationEnd)).subscribe(() => {
            this.syncSelectedRootWithCurrentUrl();
        });
    }

    setSelectedRoot(menu: AppMenuModel) {
        this.prepareSidebarExpansion(menu, this.router.url);
        this.selectedRootMenuSubject.next(menu);
        this.sidebarMenuSubject.next(menu.items || []);
    }

    getSelectedRootSnapshot(): AppMenuModel | null {
        return this.selectedRootMenuSubject.value;
    }

    syncSelectedRootWithCurrentUrl(): boolean {
        const menu = this.menuSubject.value;
        if (!menu.length) {
            return false;
        }

        const root = this.findRootByUrl(menu, this.router.url);
        if (!root) {
            return false;
        }

        if (this.isSameRoot(this.selectedRootMenuSubject.value, root)) {
            this.prepareSidebarExpansion(root, this.router.url);
            this.sidebarMenuSubject.next(root.items || []);
            return true;
        }

        this.setSelectedRoot(root);
        return true;
    }

    loadMenus(): Observable<AppMenuModel[]> {
        if (this.menuSubject.value.length) {
            return of(this.menuSubject.value);
        }

        if (this.loadMenusRequest$) {
            return this.loadMenusRequest$;
        }

        this.loadMenusRequest$ = this.apiService.get<AppMenuModel[]>(API_ENDPOINTS.identityService.menus.base).pipe(
            map((res) => this.normalizeMenus(res)),
            map((flat) => this.buildTree(flat)),
            map((tree) => this.assignRouterLinks(tree)),
            tap((menu) => {
                const root = this.findRootByUrl(menu, this.router.url);
                if (root) {
                    this.setSelectedRoot(root);
                } else if (menu.length) {
                    this.setSelectedRoot(menu[0]);
                }

                this.menuSubject.next(menu);
            }),
            tap(() => {
                this.loadMenusRequest$ = undefined;
            }),
            shareReplay(1)
        );

        return this.loadMenusRequest$;
    }

    buildTree(flat: AppMenuModel[]): AppMenuModel[] {
        const map = new Map<string, AppMenuModel>();

        flat.forEach((item) => {
            const node: AppMenuModel = { ...item, items: [] };
            const keys = this.getNodeKeys(item);
            if (!keys.length) {
                return;
            }

            keys.forEach((key) => map.set(key, node));
        });

        const roots: AppMenuModel[] = [];

        flat.forEach((item) => {
            const node = this.resolveNode(item, map);
            if (!node) {
                return;
            }

            const parentKey = this.getParentKey(item);
            if (parentKey) {
                map.get(parentKey)?.items?.push(node);
            } else {
                roots.push(node);
            }
        });

        const sortFn = (a: AppMenuModel, b: AppMenuModel) => {
            const orderDiff = (a.displayOrder ?? Number.MAX_SAFE_INTEGER) - (b.displayOrder ?? Number.MAX_SAFE_INTEGER);
            if (orderDiff !== 0) {
                return orderDiff;
            }

            return (a.code || a.label || '').localeCompare(b.code || b.label || '');
        };

        const sortTree = (nodes: AppMenuModel[]) => {
            nodes.sort(sortFn);
            nodes.forEach((node) => node.items && sortTree(node.items));
        };

        sortTree(roots);
        return roots;
    }

    getMenu(): Observable<AppMenuModel[]> {
        return this.menu$;
    }

    getRootMenus(): Observable<AppMenuModel[]> {
        return this.menu$;
    }

    getMenuSnapshot(): AppMenuModel[] {
        return this.menuSubject.value;
    }

    hasLoadedMenus(): boolean {
        return this.menuSubject.value.length > 0;
    }

    clear(): void {
        this.menuSubject.next([]);
        this.sidebarMenuSubject.next([]);
        this.selectedRootMenuSubject.next(null);
        this.loadMenusRequest$ = undefined;
    }

    private normalizeMenus(items: MenuApiItem[]): AppMenuModel[] {
        return items.map((item) => ({
            id: item.id,
            code: item.code ?? '',
            label: this.normalizeLabel(item.label),
            topbarLabel: this.normalizeOptionalLabel(item.topbarLabel),
            sidebarLabel: this.normalizeOptionalLabel(item.sidebarLabel),
            icon: item.icon,
            navigationType: item.navigationType || (item.externalUrl ? 'EXTERNAL_LINK' : item.frontendPath ? 'INTERNAL_LINK' : 'SECTION'),
            frontendPath: item.frontendPath,
            externalUrl: item.externalUrl,
            parentCode: item.parentCode ?? undefined,
            parentId: item.parentId ?? undefined,
            displayOrder: item.displayOrder,
            visible: item.visible !== false,
            permissionCode: item.permissionCode
        }));
    }

    private normalizeLabel(label?: string): string {
        if (!label) {
            return '';
        }

        return label.trim().replace(/\s+\d+$/, '');
    }

    private normalizeOptionalLabel(label?: string | null): string | undefined {
        const normalized = this.normalizeLabel(label ?? undefined);
        return normalized || undefined;
    }

    private assignRouterLinks(nodes: AppMenuModel[]): AppMenuModel[] {
        return nodes.map((node) => ({
            ...node,
            routerLink: node.navigationType === 'INTERNAL_LINK' && node.frontendPath ? [node.frontendPath] : undefined,
            url: node.navigationType === 'EXTERNAL_LINK' ? node.externalUrl : undefined,
            target: node.navigationType === 'EXTERNAL_LINK' ? '_blank' : undefined,
            items: node.items?.length ? this.assignRouterLinks(node.items) : []
        }));
    }

    private findRootByUrl(menu: AppMenuModel[], url: string): AppMenuModel | null {
        for (const root of menu) {
            if (this.containsRoute(root, url)) {
                return root;
            }
        }

        return null;
    }

    private containsRoute(node: AppMenuModel, url: string): boolean {
        if (node.navigationType === 'INTERNAL_LINK' && node.frontendPath && this.menuRouteAccessService.matchesMenuRoute(url, node.frontendPath)) {
            return true;
        }

        return node.items?.some((child) => this.containsRoute(child, url)) ?? false;
    }

    private getNodeKeys(item: AppMenuModel): string[] {
        const keys: string[] = [];
        if (item.id) {
            keys.push(`id:${item.id}`);
        }
        if (item.code) {
            keys.push(`code:${item.code}`);
        }
        return keys;
    }

    private resolveNode(item: AppMenuModel, map: Map<string, AppMenuModel>): AppMenuModel | undefined {
        if (item.id) {
            const byId = map.get(`id:${item.id}`);
            if (byId) {
                return byId;
            }
        }

        if (item.code) {
            return map.get(`code:${item.code}`);
        }

        return undefined;
    }

    private getParentKey(item: AppMenuModel): string | null {
        if (item.parentCode) {
            return `code:${item.parentCode}`;
        }
        if (item.parentId) {
            return `id:${item.parentId}`;
        }
        return null;
    }

    private isSameRoot(left: AppMenuModel | null, right: AppMenuModel | null): boolean {
        if (!left || !right) {
            return false;
        }

        if (left.id && right.id) {
            return left.id === right.id;
        }

        return !!left.code && left.code === right.code;
    }

    private prepareSidebarExpansion(menu: AppMenuModel, currentUrl: string) {
        const sidebarItems = menu.items ?? [];
        if (!sidebarItems.length) {
            return;
        }

        sidebarItems.forEach((item) => this.collapseTree(item));
        const branchWithActiveRoute = sidebarItems.find((item) => this.containsRoute(item, currentUrl));
        if (branchWithActiveRoute) {
            this.expandActivePath(branchWithActiveRoute, currentUrl);
        }
    }

    private collapseTree(node: AppMenuModel) {
        node.expanded = false;
        (node.items ?? []).forEach((child) => this.collapseTree(child));
    }

    private expandActivePath(node: AppMenuModel, url: string): boolean {
        const selfMatches = node.navigationType === 'INTERNAL_LINK' && !!node.frontendPath && this.menuRouteAccessService.matchesMenuRoute(url, node.frontendPath);
        const childMatches = (node.items ?? []).some((child) => this.expandActivePath(child, url));

        node.expanded = selfMatches || childMatches;
        return selfMatches || childMatches;
    }
}

