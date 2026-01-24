import { Routes } from '@angular/router';
import { CSMAccess } from '@pages/auth/access/csm-access.component';
import { CSMError } from '@pages/auth/error/csm-error.component';
import { CSMLogin } from '@pages/auth/login/csm-login.component';

const routes: Routes = [
  { path: 'login', component: CSMLogin },
  { path: 'access', component: CSMAccess },
  { path: 'error', component: CSMError },

  // fallback inside auth
  { path: '**', redirectTo: 'login' },
   { path: '', redirectTo: 'login', pathMatch: 'full' }
];

export default routes;
