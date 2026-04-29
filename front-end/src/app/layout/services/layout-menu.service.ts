import { Injectable } from '@angular/core';
import {BehaviorSubject, map, Observable, of, shareReplay, tap} from 'rxjs';
import { AppApiService } from '@services/auth/app-api.service';
import {AppMenuModel} from "@shared/models/app-menu.model";

@Injectable({
    providedIn: 'root'
})
export class LayoutMenuService {

    private menuSubject = new BehaviorSubject<AppMenuModel[]>([]);
    private menu$ = this.menuSubject.asObservable();

    private selectedRootMenuSubject = new BehaviorSubject<AppMenuModel | null>(null);
    selectedRootMenu$ = this.selectedRootMenuSubject.asObservable();

    constructor(
        private apiService: AppApiService
    ) {}

    setSelectedRoot(menu: AppMenuModel) {
        this.selectedRootMenuSubject.next(menu);
    }

    /**
     * Load app-menu-item from API (with RBAC + caching)
     */
     loadMenus(): Observable<AppMenuModel[]> {
         return this.apiService.get<AppMenuModel[]>('menu.get').pipe(
             map(menu => this.mapToMenuItems(menu)),
             tap(menu => this.menuSubject.next(menu)),   // ✅ store in state
             shareReplay(1)                              // ✅ cache result
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

            menuItem.items = item.items? this.mapToMenuItems(item.items): [];

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

    /*loadMenus(): Observable<AppMenuModel[]> {
    const app-menu-item: AppMenuModel[] = [
        {
            "label": "Home",
            "items": [
                {
                    "label": "Dashboard",
                    "icon": "pi pi-fw pi-home",
                    "routerLink": [
                        "/"
                    ]
                }
            ]
        },
        {
            "label": "Settings",
            "icon": "pi pi-fw pi-briefcase",
            "items": [
                {
                    "label": "Organization",
                    "icon": "pi pi-fw pi-globe",
                    "items": [
                        {
                            "label": "All Organizations",
                            "icon": "pi pi-fw pi-organization-list",
                            "routerLink": [
                                "/organizations"
                            ]
                        },
                        {
                            "label": "Create Organization",
                            "icon": "pi pi-fw pi-plus",
                            "routerLink": [
                                "/organizations/create"
                            ]
                        }
                    ]
                }
            ]
        },
        {
            "label": "Pages",
            "icon": "pi pi-fw pi-briefcase",
            "routerLink": [
                "/pages"
            ],
            "items": [
                {
                    "label": "Landing",
                    "icon": "pi pi-fw pi-globe",
                    "routerLink": [
                        "/landing"
                    ]
                },
                {
                    "label": "Auth",
                    "icon": "pi pi-fw pi-user",
                    "items": [
                        {
                            "label": "Login",
                            "icon": "pi pi-fw pi-sign-in",
                            "routerLink": [
                                "/auth/login"
                            ]
                        },
                        {
                            "label": "Error",
                            "icon": "pi pi-fw pi-times-circle",
                            "routerLink": [
                                "/auth/error"
                            ]
                        },
                        {
                            "label": "Access Denied",
                            "icon": "pi pi-fw pi-lock",
                            "routerLink": [
                                "/auth/access"
                            ]
                        }
                    ]
                },
                {
                    "label": "Product",
                    "icon": "pi pi-fw pi-pencil",
                    "routerLink": [
                        "/pages/product"
                    ]
                },
                {
                    "label": "Not Found",
                    "icon": "pi pi-fw pi-exclamation-circle",
                    "routerLink": [
                        "/pages/notfound"
                    ]
                },
                {
                    "label": "Empty",
                    "icon": "pi pi-fw pi-circle-off",
                    "routerLink": [
                        "/pages/empty"
                    ]
                }
            ]
        },
        {
            'label': 'Hierarchy',
            'items': [
                {
                    'label': 'Submenu 1',
                    'icon': 'pi pi-fw pi-bookmark',
                    'items': [
                        {
                            'label': 'Submenu 1.1',
                            'icon': 'pi pi-fw pi-bookmark',
                            'items': [
                                {
                                    'label': 'Submenu 1.1.1',
                                    'icon': 'pi pi-fw pi-bookmark'
                                }
                            ]
                        }
                    ]
                },
                {
                    'label': 'Submenu 2',
                    'icon': 'pi pi-fw pi-bookmark',
                    'items': [
                        {
                            'label': 'Submenu 2.1',
                            'icon': 'pi pi-fw pi-bookmark',
                            'items': [
                                {
                                    'label': 'Submenu 2.1.1',
                                    'icon': 'pi pi-fw pi-bookmark'
                                },
                                {
                                    'label': 'Submenu 2.1.2',
                                    'icon': 'pi pi-fw pi-bookmark'
                                }
                            ]
                        }
                    ]
                }
            ]
        }

    ];

    return of(app-menu-item);

}*/
}
