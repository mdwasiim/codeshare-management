import { Routes } from '@angular/router';
import { Documentation } from './documentation/documentation';
import { CSMEmpty } from './empty/empty';
import { Crud } from './crud/csm-crud';

export default [
    { path: 'documentation', component: Documentation },
    { path: 'crud', component: Crud },
    { path: 'empty', component: CSMEmpty }
] as Routes;
