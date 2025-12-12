import { Routes } from '@angular/router';
import { CsmAccess } from './access';
import { CsmLogin } from './login';
import { CsmError } from './error';

export default [
    { path: 'access', component: CsmAccess },
    { path: 'error', component: CsmError },
    { path: 'login', component: CsmLogin }
] as Routes;
