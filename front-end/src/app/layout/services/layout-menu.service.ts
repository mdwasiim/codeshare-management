import { Injectable } from '@angular/core';
import {BehaviorSubject, map, Observable, of, shareReplay, tap} from 'rxjs';
import { AppApiService } from '@services/auth/app-api.service';
import {AppMenuModel} from "@shared/models/app-menu.model";

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
        private apiService: AppApiService
    ) {}

    setSelectedRoot(menu: AppMenuModel) {
        this.selectedRootMenuSubject.next(menu);

        // 🔥 Load sidebar from children
        this.sidebarMenuSubject.next(menu.items || []);
    }

    /**
     * Load app-menu-item from API (with RBAC + caching)
     */
    loadMenus(): Observable<AppMenuModel[]> {
        return this.apiService.get<AppMenuModel[]>('menu.base').pipe(

            // 🔥 normalize backend response
            map(res => this.normalizeMenus(res)),

            // 🔥 build tree from flat structure
            map(flat => this.buildTree(flat)),

            tap(menu => {
                this.menuSubject.next(menu);

                // 🔥 Auto select first root
                const firstRoot = menu[0];
                if (firstRoot) {
                    this.setSelectedRoot(firstRoot);
                }
            }),

            shareReplay(1)
        );
    }
    private buildTree(flat: AppMenuModel[]): AppMenuModel[] {
        const map = new Map<string, AppMenuModel>();

        // create map
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

        // optional: sort by displayOrder
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
     * Transform + RBAC filter
     */
    private normalizeMenus(items: any[]): AppMenuModel[] {
        return items.map(item => ({
            id: item.id,
            label: item.label,
            icon: item.icon,
            route: item.route,

            // 🔥 THIS IS THE FIX
            routerLink: item.route ? [item.route] : undefined,

            parentId: item.parentId,
            displayOrder: item.displayOrder,

            visible: item.visible !== false
        }));
    }
    /**
     * Components subscribe here
     */
    getMenu(): Observable<AppMenuModel[]> {
        return this.menu$;
    }

    /**
     * Clear on logout
     */
    clear(): void {
        this.menuSubject.next([]);
    }

    getRootMenus(): Observable<AppMenuModel[]> {
        return this.menu$;
    }
}
