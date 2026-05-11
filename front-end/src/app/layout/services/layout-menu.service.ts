import { Injectable } from '@angular/core';
import { BehaviorSubject, map, Observable, shareReplay, tap } from 'rxjs';
import { AppMenuModel } from '@features/access-management/iam/models/app-menu.model';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import {AppApiService} from "@core/config/app-api.service";

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
        this.router.events
            .pipe(filter(event => event instanceof NavigationEnd))
            .subscribe(() => {
                const menu = this.menuSubject.value;
                if (!menu.length) return;

                const root = this.findRootByUrl(menu, this.router.url);
                if (root) {
                    this.setSelectedRoot(root);
                }
            });
    }

    setSelectedRoot(menu: AppMenuModel) {
        this.selectedRootMenuSubject.next(menu);
        this.sidebarMenuSubject.next(menu.items || []);
    }

    /**
     * Load menus (API → normalize → tree → router mapping)
     */
    loadMenus(): Observable<AppMenuModel[]> {
        return this.apiService.get<AppMenuModel[]>('accessManagement.menu.base').pipe(

            map(res => this.normalizeMenus(res)),

            map(flat => this.buildTree(flat)),

            // ✅ assign routerLink ONLY for leaf nodes
            map(tree => this.assignRouterLinks(tree)),

            tap(menu => {
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

        flat.forEach(item => {
            map.set(item.id!, { ...item, items: [] });
        });

        const roots: AppMenuModel[] = [];

        flat.forEach(item => {
            const node = map.get(item.id!);

            if (item.parentId) {
                const parent = map.get(item.parentId);
                parent?.items?.push(node!);
            } else {
                roots.push(node!);
            }
        });

        const sortFn = (a: AppMenuModel, b: AppMenuModel) =>
            (a.displayOrder ?? 0) - (b.displayOrder ?? 0);

        const sortTree = (nodes: AppMenuModel[]) => {
            nodes.sort(sortFn);
            nodes.forEach(n => n.items && sortTree(n.items));
        };

        sortTree(roots);

        return roots;
    }

    /**
     * Normalize backend response (NO routerLink here)
     */
    private normalizeMenus(items: any[]): AppMenuModel[] {
        return items.map(item => ({
            id: item.id,
            code: item.code,
            label: item.label,
            icon: item.icon,
            route: item.route,
            parentId: item.parentId,
            displayOrder: item.displayOrder,
            visible: item.visible !== false
        }));
    }

    /**
     * ✅ Assign routerLink ONLY to leaf nodes
     */
    private assignRouterLinks(nodes: AppMenuModel[]): AppMenuModel[] {
        return nodes.map(node => {
            const hasChildren = node.items && node.items.length > 0;

            return {
                ...node,
                routerLink: (!hasChildren && node.route)
                    ? [node.route]
                    : undefined,
                items: hasChildren
                    ? this.assignRouterLinks(node.items!)
                    : []
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
        if (node.route && url.startsWith(node.route)) {
            return true;
        }

        return node.items?.some(child => this.containsRoute(child, url)) ?? false;
    }
}
