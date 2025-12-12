import { Routes } from '@angular/router';
import { CsmLayout } from '@/layout/csm.layout';
import { CsmDashboard } from '@/pages/dashboard/csm.dashboard';
import { Documentation } from '@/pages/documentation/documentation';
import { CsmLanding } from '@/pages/landing/landing';
import { CsmNotfound } from '@/pages/notfound/notfound';

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
    { path: 'landing', component: CsmLanding },
    { path: 'notfound', component: CsmNotfound },
    { path: 'auth', loadChildren: () => import('./app/pages/auth/auth.routes') },
    { path: '**', redirectTo: '/notfound' }
];
