import { Routes } from '@angular/router';
import { Documentation } from './documentation/documentation';
import { CSMEmpty } from './empty/empty';
import { CSMProduct } from './product/csm-product';


export default [
    { path: 'documentation', component: Documentation },
    { path: 'crud', component: CSMProduct },
    { path: 'empty', component: CSMEmpty }
] as Routes;
