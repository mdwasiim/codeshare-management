import { Routes } from '@angular/router';
import { CsmDashboard } from './app/pages/dashboard/csm.dashboard';
import { Documentation } from './app/pages/documentation/documentation';
import { Landing } from './app/pages/landing/landing';
import { Notfound } from './app/pages/notfound/notfound';
import { CsmLayout } from '@/layout/component/layout/csm.layout';

export const csmRoutes: Routes = [
    {
        path: '',
        component: CsmLayout,
        children: [
            { path: '', component: CsmDashboard },
            { path: 'uikit', loadChildren: () => import('./app/pages/uikit/uikit.routes') },
            { path: 'documentation', component: Documentation },
            { path: 'pages', loadChildren: () => import('./app/pages/pages.routes') }
        ]
    },
    { path: 'landing', component: Landing },
    { path: 'notfound', component: Notfound },
    { path: 'auth', loadChildren: () => import('./app/pages/auth/auth.routes') },
    { path: '**', redirectTo: '/notfound' }
];
