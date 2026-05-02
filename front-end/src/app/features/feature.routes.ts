import { Routes } from '@angular/router';
import {IAM_ROUTES} from "@features/routes/iam.routes";
import {SETTINGS_ROUTES} from "@features/routes/settings.routes";


export const FEATURE_ROUTES: Routes = [
    ...IAM_ROUTES,
    ...SETTINGS_ROUTES
];
