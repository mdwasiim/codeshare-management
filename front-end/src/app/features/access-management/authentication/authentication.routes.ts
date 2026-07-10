import { LoginPageComponent } from '@features/access-management/authentication/pages/login/login-page.component';
import { Routes } from '@angular/router';

export const AUTHENTICATION_ROUTES: Routes = [
    {
        path: 'login',
        loadComponent: () => import('@features/access-management/authentication/pages/login/login-page.component').then((m) => m.LoginPageComponent),
        data: {
            title: 'Login'
        }
    },
    {
        path: 'oidc/callback',
        loadComponent: () => import('@features/access-management/authentication/pages/oidc-callback/oidc-callback.component').then((m) => m.OidcCallbackComponent),
        data: {
            title: 'OIDC Callback'
        }
    },
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    }
];
