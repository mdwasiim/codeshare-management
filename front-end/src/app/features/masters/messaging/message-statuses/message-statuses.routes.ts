import { Routes } from '@angular/router';

export const MESSAGESTATUS_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/message-statuses-list/message-statuses-list.page').then((m) => m.MessageStatusListPage) },
    { path: 'create', loadComponent: () => import('./pages/message-statuses-form/message-statuses-form.page').then((m) => m.MessageStatusFormPage) },
    { path: ':id', loadComponent: () => import('./pages/message-statuses-form/message-statuses-form.page').then((m) => m.MessageStatusFormPage) }
];
