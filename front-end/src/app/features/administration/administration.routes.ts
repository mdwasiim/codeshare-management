import { Routes } from '@angular/router';
import { ACCESS_MANAGEMENT_ROUTES } from './access-management/access-management.routes';

export const ADMINISTRATION_ROUTES: Routes = [
    ...ACCESS_MANAGEMENT_ROUTES
];
