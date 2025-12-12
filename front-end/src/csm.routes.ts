import { Routes } from '@angular/router';
import { CSMLayout } from '@layout/csm.layout';
import { CSMDashboard } from '@pages/dashboard/csm.dashboard';
import { Documentation } from '@pages/documentation/documentation';
import { CSMLanding } from '@pages/landing/landing';
import { CSMNotfound } from '@pages/notfound/notfound';

export const csmRoutes: Routes = [
    {
        path: '',
        component: CSMLayout,
        children: [
            { path: '', component: CSMDashboard },

            // Lazy-loaded UI Kit
            { path: 'uikit', loadChildren: () => import('@pages/uikit/uikit.routes') },

            { path: 'documentation', component: Documentation },

            // Lazy-loaded Pages Module
            { path: 'pages', loadChildren: () => import('@pages/pages.routes') }
        ]
    },

    { path: 'landing', component: CSMLanding },
    { path: 'notfound', component: CSMNotfound },

    // Lazy-loaded Auth Module
    { path: 'auth', loadChildren: () => import('@pages/auth/auth.routes') },

    // Global Wildcard
    { path: '**', redirectTo: 'notfound' }
];
