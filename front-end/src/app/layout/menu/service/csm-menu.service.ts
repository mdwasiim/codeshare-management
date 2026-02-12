import { Injectable } from '@angular/core';
import { BehaviorSubject, map, Observable, of, tap } from 'rxjs';
import { CSMApiService } from '../../../core/services/auth/csm-api.service';
import { CSMMenuItemModel } from '../../../core/models/csm-menu.model';

@Injectable({
  providedIn: 'root'
})
export class CSMMenuService {

  // 🔥 Shared menu state
  private menuSubject = new BehaviorSubject<CSMMenuItemModel[]>([]);
  private menu$ = this.menuSubject.asObservable();

  constructor(private apiService: CSMApiService) {}

  /**
   * Call this ONCE after login
   */
  loadMenus(): Observable<CSMMenuItemModel[]> {
    return this.apiService.get<any[]>('menu').pipe(
      map(data => this.mapToMenuItems(data))
    );
  }

  private mapToMenuItems(items: any[]): CSMMenuItemModel[] {
    return items.map(item => {
      const menuItem: CSMMenuItemModel = {
        label: item.label,
        icon: item.icon
      };

      // children → submenu
      if (item.items?.length) {
        menuItem.items = this.mapToMenuItems(item.items);
      }

      // leaf → navigate
      if ((!item.items || item.items.length === 0) && item.routerLink?.length) {
        menuItem.routerLink = item.routerLink;
      }

      return menuItem;
    });
  }

//  loadMenus(): Observable<CSMMenuItemModel[]> {
//     const menu: CSMMenuItemModel[] = [
//     {
//         "label": "Home",
//         "items": [
//             {
//                 "label": "Dashboard",
//                 "icon": "pi pi-fw pi-home",
//                 "routerLink": [
//                     "/"
//                 ]
//             }
//         ]
//     },
//     {
//         "label": "Settings",
//         "icon": "pi pi-fw pi-briefcase",
//         "items": [
//             {
//                 "label": "Organization",
//                 "icon": "pi pi-fw pi-globe",
//                 "items": [
//                     {
//                         "label": "All Organizations",
//                         "icon": "pi pi-fw pi-list",
//                         "routerLink": [
//                             "/organizations"
//                         ]
//                     },
//                     {
//                         "label": "Create Organization",
//                         "icon": "pi pi-fw pi-plus",
//                         "routerLink": [
//                             "/organizations/create"
//                         ]
//                     }
//                 ]
//             }
//         ]
//     },
//     {
//         "label": "Pages",
//         "icon": "pi pi-fw pi-briefcase",
//         "routerLink": [
//             "/pages"
//         ],
//         "items": [
//             {
//                 "label": "Landing",
//                 "icon": "pi pi-fw pi-globe",
//                 "routerLink": [
//                     "/landing"
//                 ]
//             },
//             {
//                 "label": "Auth",
//                 "icon": "pi pi-fw pi-user",
//                 "items": [
//                     {
//                         "label": "Login",
//                         "icon": "pi pi-fw pi-sign-in",
//                         "routerLink": [
//                             "/auth/login"
//                         ]
//                     },
//                     {
//                         "label": "Error",
//                         "icon": "pi pi-fw pi-times-circle",
//                         "routerLink": [
//                             "/auth/error"
//                         ]
//                     },
//                     {
//                         "label": "Access Denied",
//                         "icon": "pi pi-fw pi-lock",
//                         "routerLink": [
//                             "/auth/access"
//                         ]
//                     }
//                 ]
//             },
//             {
//                 "label": "Product",
//                 "icon": "pi pi-fw pi-pencil",
//                 "routerLink": [
//                     "/pages/product"
//                 ]
//             },
//             {
//                 "label": "Not Found",
//                 "icon": "pi pi-fw pi-exclamation-circle",
//                 "routerLink": [
//                     "/pages/notfound"
//                 ]
//             },
//             {
//                 "label": "Empty",
//                 "icon": "pi pi-fw pi-circle-off",
//                 "routerLink": [
//                     "/pages/empty"
//                 ]
//             }
//         ]
//     },
// 	{
//         'label': 'Hierarchy',
//         'items': [
//             {
//                 'label': 'Submenu 1',
//                 'icon': 'pi pi-fw pi-bookmark',
//                 'items': [
//                     {
//                         'label': 'Submenu 1.1',
//                         'icon': 'pi pi-fw pi-bookmark',
//                         'items': [
//                             {
//                                 'label': 'Submenu 1.1.1',
//                                 'icon': 'pi pi-fw pi-bookmark'
//                             }
//                         ]
//                     }
//                 ]
//             },
//             {
//                 'label': 'Submenu 2',
//                 'icon': 'pi pi-fw pi-bookmark',
//                 'items': [
//                     {
//                         'label': 'Submenu 2.1',
//                         'icon': 'pi pi-fw pi-bookmark',
//                         'items': [
//                             {
//                                 'label': 'Submenu 2.1.1',
//                                 'icon': 'pi pi-fw pi-bookmark'
//                             },
//                             {
//                                 'label': 'Submenu 2.1.2',
//                                 'icon': 'pi pi-fw pi-bookmark'
//                             }
//                         ]
//                     }
//                 ]
//             }
//         ]
//     }
    
// ];

//   return of(menu);

//   }

  /**
   * Sidebar & other components subscribe here
   */
  getMenu(): Observable<CSMMenuItemModel[]> {
    return this.menu$;
  }

  /**
   * Call on logout
   */
  clear(): void {
    this.menuSubject.next([]);
  }
}
