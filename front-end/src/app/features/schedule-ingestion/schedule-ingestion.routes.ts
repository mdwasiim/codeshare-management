import { Routes } from '@angular/router';

export const SCHEDULE_INGESTION_ROUTES: Routes = [
    {
        path: 'ssim-ingestion',
        loadComponent: () => import('./loaded-schedules/ssim/ssim-loaded.page').then((m) => m.SsimLoadedPage)
    },
    {
        path: 'asm-ssm-ingestion',
        loadComponent: () => import('./loaded-schedules/asm-ssm/asm-ssm-loaded.page').then((m) => m.AsmSsmLoadedPage)
    },
    {
        path: 'schedule-comparison/:type/:fileId',
        loadComponent: () => import('./comparison/schedule-comparison.page').then((m) => m.ScheduleComparisonPage)
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
