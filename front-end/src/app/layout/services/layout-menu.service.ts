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
            map(menu => this.mapToMenuItems(menu)),
            tap(menu => {
                this.menuSubject.next(menu);

                // 🔥 Auto select first root
                const firstRoot = menu.find(m => !m.parentId);
                if (firstRoot) {
                    this.setSelectedRoot(firstRoot);
                }
            }),
            shareReplay(1)
        );
    }

    /**
     * Transform + RBAC filter
     */
    private mapToMenuItems(items: any[],  parentPath: string = ''): AppMenuModel[] {
        return items.map(item => {

            const currentPath =
                parentPath +
                '/' +
                (item.label || '')
                    .toLowerCase()
                    .replace(/\s+/g, '-');

            const menuItem: AppMenuModel = {
                id: item.id,
                label: item.label,
                icon: item.icon,

                routerLink: item.routerLink,

                path: currentPath,
                url: item.url,
                target: item.target,

                visible: item.visible !== false,
                separator: item.separator,

                styleClass: item.styleClass,
                badgeClass: item.badgeClass
            };

            menuItem.items = item.items
                ? this.mapToMenuItems(item.items, currentPath) // ✅ FIX
                : [];

            return menuItem;
        });
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
        return this.menu$.pipe(
            map(menus => menus.filter(m => !m.parentId))
        );
    }
}
