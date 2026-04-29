import { Routes } from '@angular/router';
import { LayoutComponent } from '@layout/shell/layout.component';
import { AuthGuard } from '@core/guards/auth.guard';
import {CSMDashboard} from "@/features/dashboard/csm.dashboard";
import {CSMLanding} from "@/features/landing/landing";
import {CSMNotfound} from "@/features/notfound/notfound";

export const csmRoutes: Routes = [

  // 🔒 Protected
  {
    path: '',
    component: LayoutComponent,
    canActivateChild: [AuthGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: CSMDashboard },
      { path: 'settings', loadChildren: () => import('@features/settings/organizations/organizations.routes').then(m => m.ORGANIZATION_ROUTES) },
      { path: 'pages', loadChildren: () => import('@features/pages.routes') }

    ]
  },

  // 🔓 Public
  { path: 'auth', loadChildren: () => import('@features/auth/auth.routes') },
  { path: 'landing', component: CSMLanding },
  { path: 'notfound', component: CSMNotfound },
  { path: '**', redirectTo: 'notfound' }
];




