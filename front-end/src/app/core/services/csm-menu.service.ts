import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { CSMApiService } from './auth/csm-api.service';
import { CSMMenuItemModel } from '../models/csm-menu.model';

@Injectable({
  providedIn: 'root'
})
export class CSMMenuService {

  constructor(private csmApiService: CSMApiService) {}

  /**
   * Fetch menu from backend
   */
  getMenu(): Observable<CSMMenuItemModel[]> {
    //return this.csmApiService.get('menu'); // uses API_CONFIG.endpoints.menu
    const menu: CSMMenuItemModel[] = [
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
                        "icon": "pi pi-fw pi-list",
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

    return of(menu);
  }
}
