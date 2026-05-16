import { Routes } from '@angular/router';

export const SCHEDULE_INGESTION_ROUTES: Routes = [
    {
        path: 'ssim-ingestion',
        loadComponent: () => import('./pages/loaded-schedules/loaded-schedules.page').then((m) => m.LoadedSchedulesPage),
        data: { mode: 'SSIM' }
    },
    {
        path: 'asm-ssm-ingestion',
        loadComponent: () => import('./pages/loaded-schedules/loaded-schedules.page').then((m) => m.LoadedSchedulesPage),
        data: { mode: 'ASM_SSM' }
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
