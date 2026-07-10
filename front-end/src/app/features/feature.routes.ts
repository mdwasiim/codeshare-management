import { Routes } from '@angular/router';
import { IDENTITY_ROUTES } from '@features/access-management/identity/identity.routes';
import { AUTHORIZATION_ROUTES } from '@features/access-management/authorization/authorization.routes';
import { SETTINGS_ROUTES } from '@features/settings/settings.routes';
import { ASSIGNMENTS_ROUTES } from '@features/access-management/assignments/assignments.routes';
import { AUTHENTICATION_ROUTES } from '@features/access-management/authentication/authentication.routes';
import { DASHBOARD_ROUTES } from '@features/home/dashboard/dashboard.routes';
import { SCHEDULE_INGESTION_ROUTES } from '@features/schedule-ingestion/schedule-ingestion.routes';
import { MASTERS_ROUTES } from '@features/masters/masters.routes';
import { TENANT_ONBOARDING_ROUTES } from '@features/tenant-onboarding/tenant-onboarding.routes';

export const FEATURE_ROUTES: Routes = [
    ...ASSIGNMENTS_ROUTES,
    ...AUTHENTICATION_ROUTES,
    ...TENANT_ONBOARDING_ROUTES,
    ...IDENTITY_ROUTES,
    ...AUTHORIZATION_ROUTES,
    ...DASHBOARD_ROUTES,
    ...SETTINGS_ROUTES,
    {
        path: 'masters',
        children: MASTERS_ROUTES
    },
    {
        path: 'settings',
        children: SETTINGS_ROUTES
    },
    ...SCHEDULE_INGESTION_ROUTES
];
