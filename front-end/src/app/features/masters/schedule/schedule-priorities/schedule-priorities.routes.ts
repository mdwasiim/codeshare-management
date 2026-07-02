import { Routes } from '@angular/router';

export const SCHEDULEPRIORITY_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/schedule-priorities-list/schedule-priorities-list.page').then((m) => m.SchedulePriorityListPage) },
    { path: 'create', loadComponent: () => import('./pages/schedule-priorities-form/schedule-priorities-form.page').then((m) => m.SchedulePriorityFormPage) },
    { path: ':id', loadComponent: () => import('./pages/schedule-priorities-form/schedule-priorities-form.page').then((m) => m.SchedulePriorityFormPage) }
];
