import { Routes } from '@angular/router';

import { TENANT_ADMINISTRATION_ROUTES } from './tenant-administration/tenant-administration.routes';
import { TENANT_IDENTITY_ROUTES } from './tenant-identity/tenant-identity.routes';
import { TENANT_INGESTION_ROUTES } from './tenant-ingestion/tenant-ingestion.routes';
import { TENANT_PARTNER_ROUTES } from './tenant-partner-management/tenant-partner.routes';

export const TENANT_ONBOARDING_ROUTES: Routes = [
    {
        path: 'tenant-setup',
        loadComponent: () => import('./tenant-setup/pages/tenant-setup/tenant-setup.page').then((m) => m.TenantSetupPage)
    },
    ...TENANT_ADMINISTRATION_ROUTES,
    ...TENANT_IDENTITY_ROUTES,
    ...TENANT_INGESTION_ROUTES,
    ...TENANT_PARTNER_ROUTES
];
