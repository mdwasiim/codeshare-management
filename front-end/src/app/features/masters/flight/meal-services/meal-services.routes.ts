import { Routes } from '@angular/router';

export const MEALSERVICE_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/meal-services-list/meal-services-list.page').then((m) => m.MealServiceListPage) },
    { path: 'create', loadComponent: () => import('./pages/meal-services-form/meal-services-form.page').then((m) => m.MealServiceFormPage) },
    { path: ':id', loadComponent: () => import('./pages/meal-services-form/meal-services-form.page').then((m) => m.MealServiceFormPage) }
];
