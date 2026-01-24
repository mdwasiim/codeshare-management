import { Routes } from '@angular/router';
import { CSMEmpty } from './empty/empty';
import { CSMProduct } from './product/csm-product';


export default [
    { path: 'product', component: CSMProduct },
    { path: 'empty', component: CSMEmpty },
    { path: '**', redirectTo: '/notfound' }
] as Routes;
