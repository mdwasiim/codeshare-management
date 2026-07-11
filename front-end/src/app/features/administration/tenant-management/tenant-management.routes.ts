import { Routes } from '@angular/router';

import { TENANT_ONBOARDING_ROUTES } from './tenant-onboarding/tenant-onboarding.routes';

export const TENANT_MANAGEMENT_ROUTES: Routes = [
    ...TENANT_ONBOARDING_ROUTES
];
