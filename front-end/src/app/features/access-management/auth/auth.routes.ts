import { LoginPageComponent } from '@features/access-management/auth/pages/login/login-page.component';
import { Routes } from '@angular/router';

export const AUTH_ROUTES: Routes = [
    {
        path: 'login',
        loadComponent: () => import('@features/access-management/auth/pages/login/login-page.component').then((m) => m.LoginPageComponent),
        data: {
            title: 'Login'
        }
    },
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    }
];
