export const MENUS_ROUTES = [
    {
        path: '',
        loadComponent: () => import('./pages/menu-list/menu-list.page').then((m) => m.MenuListPage)
    },
    {
        path: 'create',
        loadComponent: () => import('./pages/menu-form/menu-form.page').then((m) => m.MenuFormPage)
    },
    {
        path: ':id',
        loadComponent: () => import('./pages/menu-form/menu-form.page').then((m) => m.MenuFormPage)
    }
];
