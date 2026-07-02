import { Routes } from '@angular/router';

export const SCHEDULECHANNEL_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/schedule-channels-list/schedule-channels-list.page').then((m) => m.ScheduleChannelListPage) },
    { path: 'create', loadComponent: () => import('./pages/schedule-channels-form/schedule-channels-form.page').then((m) => m.ScheduleChannelFormPage) },
    { path: ':id', loadComponent: () => import('./pages/schedule-channels-form/schedule-channels-form.page').then((m) => m.ScheduleChannelFormPage) }
];
