import { Routes } from '@angular/router';
import { ACCESS_MANAGEMENT_ROUTES } from './access-management/access-management.routes';
import { TENANT_MANAGEMENT_ROUTES } from './tenant-management/tenant-management.routes';

export const ADMINISTRATION_ROUTES: Routes = [
    ...ACCESS_MANAGEMENT_ROUTES,
    ...TENANT_MANAGEMENT_ROUTES
];
