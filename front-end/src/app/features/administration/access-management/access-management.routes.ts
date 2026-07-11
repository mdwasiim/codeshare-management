import { Routes } from '@angular/router';
import { IDENTITY_ROUTES } from './identity/identity.routes';
import { AUTHORIZATION_ROUTES } from './authorization/authorization.routes';
import { ASSIGNMENTS_ROUTES } from './assignments/assignments.routes';

export const ACCESS_MANAGEMENT_ROUTES: Routes = [
    ...ASSIGNMENTS_ROUTES,
    ...IDENTITY_ROUTES,
    ...AUTHORIZATION_ROUTES
];
