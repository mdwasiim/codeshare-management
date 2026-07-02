import { Routes } from '@angular/router';

export const MESSAGEPRIORITY_ROUTES: Routes = [
    { path: '', loadComponent: () => import('./pages/message-priorities-list/message-priorities-list.page').then((m) => m.MessagePriorityListPage) },
    { path: 'create', loadComponent: () => import('./pages/message-priorities-form/message-priorities-form.page').then((m) => m.MessagePriorityFormPage) },
    { path: ':id', loadComponent: () => import('./pages/message-priorities-form/message-priorities-form.page').then((m) => m.MessagePriorityFormPage) }
];
