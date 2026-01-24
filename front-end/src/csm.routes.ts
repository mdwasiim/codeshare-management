import { Routes } from '@angular/router';
import { CSMLayout } from '@layout/csm.layout';
import { CSMDashboard } from '@pages/dashboard/csm.dashboard';
import { CSMLanding } from '@pages/landing/landing';
import { CSMNotfound } from '@pages/notfound/notfound';
import { AuthGuard } from '@core/guards/auth.guard';

export const csmRoutes: Routes = [

  // 🔒 Protected
  {
    path: '',
    component: CSMLayout,
    canActivateChild: [AuthGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: CSMDashboard },
      { path: 'settings', loadChildren: () => import('@pages/settings/organizations/organizations.routes').then(m => m.ORGANIZATION_ROUTES) },
      { path: 'pages', loadChildren: () => import('@pages/pages.routes') }
    
    ]
  },

  // 🔓 Public
  { path: 'auth', loadChildren: () => import('@pages/auth/auth.routes') },
  { path: 'landing', component: CSMLanding },
  { path: 'notfound', component: CSMNotfound },
  { path: '**', redirectTo: 'notfound' }
];




