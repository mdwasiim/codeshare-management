import { Routes } from '@angular/router';
import { CSMAccess } from './access';
import { CSMLogin } from './login';
import { CSMError } from './error';

export default [
    { path: 'access', component: CSMAccess },
    { path: 'error', component: CSMError },
    { path: 'login', component: CSMLogin }
] as Routes;
