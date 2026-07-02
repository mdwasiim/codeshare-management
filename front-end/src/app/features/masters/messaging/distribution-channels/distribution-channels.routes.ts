import { Routes } from '@angular/router';

export const DISTRIBUTIONCHANNEL_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/distribution-channels-list/distribution-channels-list.page').then((m) => m.DistributionChannelListPage) },
    { path: 'create', loadComponent: () => import('./pages/distribution-channels-form/distribution-channels-form.page').then((m) => m.DistributionChannelFormPage) },
    { path: ':id', loadComponent: () => import('./pages/distribution-channels-form/distribution-channels-form.page').then((m) => m.DistributionChannelFormPage) }
];
