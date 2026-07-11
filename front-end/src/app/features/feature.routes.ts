import { Routes } from '@angular/router';
import { ADMINISTRATION_ROUTES } from '@features/administration/administration.routes';
import { SCHEDULE_INGESTION_ROUTES } from '@features/schedule-ingestion/schedule-ingestion.routes';
import { MASTERS_ROUTES } from '@features/masters/masters.routes';
import {DASHBOARD_ROUTES} from "@features/home/dashboard/dashboard.routes";

export const FEATURE_ROUTES: Routes = [
     ...DASHBOARD_ROUTES,
    {
        path: 'administration',
        redirectTo: 'tenants',
        pathMatch: 'full'
    },
    {
        path: 'masters',
        redirectTo: 'masters/geography/regions',
        pathMatch: 'full'
    },
    ...ADMINISTRATION_ROUTES,
    {
        path: 'masters',
        children: MASTERS_ROUTES
    },
    ...SCHEDULE_INGESTION_ROUTES
];
