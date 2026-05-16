import { Routes } from '@angular/router';
import { IAM_ROUTES } from '@features/access-management/iam/iam.routes';
import { SETTINGS_ROUTES } from '@features/settings/settings.routes';
import { ASSIGNMENT_ROUTES } from '@features/access-management/assignment/assignment.routes';
import { AUTH_ROUTES } from '@features/access-management/auth/auth.routes';
import { DASHBOARD_ROUTES } from '@features/home/dashboard/dashboard.routes';
import { SCHEDULE_INGESTION_ROUTES } from '@features/schedule-ingestion/schedule-ingestion.routes';

export const FEATURE_ROUTES: Routes = [...ASSIGNMENT_ROUTES, ...AUTH_ROUTES, ...IAM_ROUTES, ...DASHBOARD_ROUTES, ...SETTINGS_ROUTES, ...SCHEDULE_INGESTION_ROUTES];
