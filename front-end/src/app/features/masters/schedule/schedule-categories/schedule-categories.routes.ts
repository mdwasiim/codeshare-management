import { Routes } from '@angular/router';

export const SCHEDULECATEGORY_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/schedule-categories-list/schedule-categories-list.page').then((m) => m.ScheduleCategoryListPage) },
    { path: 'create', loadComponent: () => import('./pages/schedule-categories-form/schedule-categories-form.page').then((m) => m.ScheduleCategoryFormPage) },
    { path: ':id', loadComponent: () => import('./pages/schedule-categories-form/schedule-categories-form.page').then((m) => m.ScheduleCategoryFormPage) }
];
