import { Routes } from '@angular/router';

export const SCHEDULE_INGESTION_ROUTES: Routes = [
    {
        path: 'ssim-ingestion',
        loadComponent: () => import('./pages/ssim-loaded/ssim-loaded.page').then((m) => m.SsimLoadedPage)
    },
    {
        path: 'ssim-ingestion/actions',
        loadComponent: () => import('./pages/ssim-actions/ssim-actions.page').then((m) => m.SsimActionsPage)
    },
    {
        path: 'asm-ssm-ingestion',
        loadComponent: () => import('./pages/asm-ssm-loaded/asm-ssm-loaded.page').then((m) => m.AsmSsmLoadedPage)
    },
    {
        path: 'asm-ssm-ingestion/actions',
        loadComponent: () => import('./pages/asm-ssm-actions/asm-ssm-actions.page').then((m) => m.AsmSsmActionsPage)
    },
    {
        path: 'upload',
        redirectTo: 'ssim-ingestion',
        pathMatch: 'full'
    },
    {
        path: 'jobs',
        redirectTo: 'asm-ssm-ingestion',
        pathMatch: 'full'
    },
    {
        path: 'errors',
        redirectTo: 'asm-ssm-ingestion',
        pathMatch: 'full'
    }
];
