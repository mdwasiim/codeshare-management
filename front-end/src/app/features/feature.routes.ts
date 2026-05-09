import { Routes } from '@angular/router';
import {IAM_ROUTES} from "@features/routes/iam.routes";
import {SETTINGS_ROUTES} from "@features/routes/settings.routes";
import {ACCESS_MANAGEMENT_ROUTES} from "@features/routes/access-management.routes";


export const FEATURE_ROUTES: Routes = [
    ...ACCESS_MANAGEMENT_ROUTES,
    ...IAM_ROUTES,
    ...SETTINGS_ROUTES
];
