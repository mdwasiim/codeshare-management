import { Routes } from '@angular/router';
import { ADMINISTRATION_ROUTES } from '@features/administration/administration.routes';
import { SCHEDULE_INGESTION_ROUTES } from '@features/schedule-ingestion/schedule-ingestion.routes';
import { MASTERS_ROUTES } from '@features/masters/masters.routes';

export const FEATURE_ROUTES: Routes = [
    ...ADMINISTRATION_ROUTES,
    {
        path: 'masters',
        children: MASTERS_ROUTES
    },
    ...SCHEDULE_INGESTION_ROUTES
];
