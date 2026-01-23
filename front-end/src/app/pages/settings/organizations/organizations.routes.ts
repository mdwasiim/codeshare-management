import { Routes } from '@angular/router';
import { OrganizationList } from './list/organization-list';
import { OrganizationDetail } from './details/organization-detail';



export const ORGANIZATION_ROUTES: Routes = [
  { path: '', component: OrganizationList },
  {
    path: ':id',
    component: OrganizationDetail,
    children: [
      { path: '', redirectTo: 'overview', pathMatch: 'full' }
      // { path: 'overview', component: OrganizationOverview },
      // { path: 'datasources', component: OrganizationDataSources },
      // { path: 'groups', component: OrganizationGroups },
      // { path: 'users', component: OrganizationUsers }
    ]
  }
];
