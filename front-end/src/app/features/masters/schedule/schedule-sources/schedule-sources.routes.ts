import { Routes } from '@angular/router';

export const SCHEDULESOURCE_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/schedule-sources-list/schedule-sources-list.page').then((m) => m.ScheduleSourceListPage) },
    { path: 'create', loadComponent: () => import('./pages/schedule-sources-form/schedule-sources-form.page').then((m) => m.ScheduleSourceFormPage) },
    { path: ':id', loadComponent: () => import('./pages/schedule-sources-form/schedule-sources-form.page').then((m) => m.ScheduleSourceFormPage) }
];
