import { Routes } from '@angular/router';

export const AUTHENTICATION_ROUTES: Routes = [
    {
        path: 'login',
        loadComponent: () => import('@core/auth/pages/login/login-page.component').then((m) => m.LoginPageComponent),
        data: {
            title: 'Login'
        }
    },
    {
        path: 'auth',
        children: [
            {
                path: 'login',
                redirectTo: '/login',
                pathMatch: 'full'
            },
            {
                path: 'oidc/callback',
                loadComponent: () => import('@core/auth/pages/oidc-callback/oidc-callback.component').then((m) => m.OidcCallbackComponent),
                data: {
                    title: 'OIDC Callback'
                }
            },
            {
                path: '',
                redirectTo: '/login',
                pathMatch: 'full'
            }
        ]
    }
];
