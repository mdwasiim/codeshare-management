import { Injectable } from '@angular/core';
import { BehaviorSubject, map, Observable, shareReplay, tap } from 'rxjs';
import { AppMenuModel } from '@features/access-management/iam/models/app-menu.model';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';

type MenuApiItem = Partial<AppMenuModel> & {
    id?: string;
    code?: string;
    label?: string;
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

    private selectedRootMenuSubject = new BehaviorSubject<AppMenuModel | null>(null);
    selectedRootMenu$ = this.selectedRootMenuSubject.asObservable();

    constructor(
        private apiService: AppApiService,
        private router: Router
    ) {
        // ✅ Keep menu in sync with route changes
        this.router.events.pipe(filter((event) => event instanceof NavigationEnd)).subscribe(() => {
            const menu = this.menuSubject.value;
            if (!menu.length) return;

            const root = this.findRootByUrl(menu, this.router.url);
            if (root) {
                this.setSelectedRoot(root);
            }
        });
    }

    setSelectedRoot(menu: AppMenuModel) {
        this.prepareSidebarExpansion(menu, this.router.url);
        this.selectedRootMenuSubject.next(menu);
        this.sidebarMenuSubject.next(menu.items || []);
    }

    /**
     * Load menus (API → normalize → tree → router mapping)
     */
    loadMenus(): Observable<AppMenuModel[]> {
        return this.apiService.get<AppMenuModel[]>(API_ENDPOINTS.accessManagement.menu.base).pipe(
            map((res) => this.normalizeMenus(res)),

            map((flat) => this.buildTree(flat)),

            // ✅ assign routerLink ONLY for leaf nodes
            map((tree) => this.assignRouterLinks(tree)),

            tap((menu) => {
                this.menuSubject.next(menu);

                const root = this.findRootByUrl(menu, this.router.url);
                if (root) {
                    this.setSelectedRoot(root);
                } else if (menu.length) {
                    this.setSelectedRoot(menu[0]);
                }
            }),

            shareReplay(1)
        );
    }

    public buildTree(flat: AppMenuModel[]): AppMenuModel[] {
        const map = new Map<string, AppMenuModel>();

        flat.forEach((item) => {
            const node: AppMenuModel = { ...item, items: [] };

            const keys = this.getNodeKeys(item);
            if (!keys.length) return;

            keys.forEach((key) => map.set(key, node));
        });

        const roots: AppMenuModel[] = [];

        flat.forEach((item) => {
            const node = this.resolveNode(item, map);
            if (!node) return;

            const parentKey = this.getParentKey(item);
            if (parentKey) {
                const parent = map.get(parentKey);
                parent?.items?.push(node);
            } else {
                roots.push(node);
            }
        });

        const sortFn = (a: AppMenuModel, b: AppMenuModel) => (a.displayOrder ?? 0) - (b.displayOrder ?? 0);

        const sortTree = (nodes: AppMenuModel[]) => {
            nodes.sort(sortFn);
            nodes.forEach((n) => n.items && sortTree(n.items));
        };

        sortTree(roots);

        return roots;
    }

    /**
     * Normalize backend response (NO routerLink here)
     */
    private normalizeMenus(items: MenuApiItem[]): AppMenuModel[] {
        return items.map((item) => ({
            id: item.id,
            code: item.code ?? '',
            label: this.normalizeLabel(item.label),
            icon: item.icon,
            route: item.route,
            parentId: item.parentId ?? undefined,
            displayOrder: item.displayOrder,
            visible: item.visible !== false
        }));
    }

    private normalizeLabel(label?: string): string {
        if (!label) return '';
        return label.trim().replace(/\s+\d+$/, '');
    }

    /**
     * ✅ Assign routerLink ONLY to leaf nodes
     */
    private assignRouterLinks(nodes: AppMenuModel[]): AppMenuModel[] {
        return nodes.map((node) => {
            const hasChildren = node.items && node.items.length > 0;

            return {
                ...node,
                routerLink: !hasChildren && node.route ? [node.route] : undefined,
                items: hasChildren ? this.assignRouterLinks(node.items!) : []
            };
        });
    }

    getMenu(): Observable<AppMenuModel[]> {
        return this.menu$;
    }

    getRootMenus(): Observable<AppMenuModel[]> {
        return this.menu$;
    }

    clear(): void {
        this.menuSubject.next([]);
        this.sidebarMenuSubject.next([]);
        this.selectedRootMenuSubject.next(null);
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
        if (node.route && this.matchesPath(url, node.route)) {
            return true;
        }

        return node.items?.some((child) => this.containsRoute(child, url)) ?? false;
    }

    private getNodeKeys(item: AppMenuModel): string[] {
        const keys: string[] = [];

        if (item.id) keys.push(`id:${item.id}`);
        if (item.code) keys.push(`code:${item.code}`);

        return keys;
    }

    private resolveNode(item: AppMenuModel, map: Map<string, AppMenuModel>): AppMenuModel | undefined {
        if (item.id) {
            const byId = map.get(`id:${item.id}`);
            if (byId) return byId;
        }

        if (item.code) {
            return map.get(`code:${item.code}`);
        }

        return undefined;
    }

    private getParentKey(item: AppMenuModel): string | null {
        const codeParent = (item as AppMenuModel & { parentCode?: string | null }).parentCode;
        if (codeParent) return `code:${codeParent}`;
        if (item.parentId) return `id:${item.parentId}`;
        return null;
    }

    private prepareSidebarExpansion(menu: AppMenuModel, currentUrl: string) {
        const sidebarItems = menu.items ?? [];
        if (!sidebarItems.length) return;

        sidebarItems.forEach((item) => this.collapseTree(item));

        const branchWithActiveRoute = sidebarItems.find((item) => this.containsRoute(item, currentUrl));
        if (branchWithActiveRoute) {
            this.expandActivePath(branchWithActiveRoute, currentUrl);
            return;
        }

        this.expandFirstBranch(sidebarItems[0]);
    }

    private collapseTree(node: AppMenuModel) {
        node.expanded = false;
        (node.items ?? []).forEach((child) => this.collapseTree(child));
    }

    private expandFirstBranch(node: AppMenuModel) {
        node.expanded = true;

        const children = node.items ?? [];
        if (!children.length) return;

        this.expandFirstBranch(children[0]);
    }

    private expandActivePath(node: AppMenuModel, url: string): boolean {
        const selfMatches = !!node.route && this.matchesPath(url, node.route);
        const childMatches = (node.items ?? []).some((child) => this.expandActivePath(child, url));

        node.expanded = childMatches;
        return selfMatches || childMatches;
    }

    private matchesPath(currentUrl: string, link: string): boolean {
        if (!currentUrl || !link) return false;
        if (currentUrl === link) return true;

        return currentUrl.startsWith(`${link}/`) || currentUrl.startsWith(`${link}?`);
    }
}
