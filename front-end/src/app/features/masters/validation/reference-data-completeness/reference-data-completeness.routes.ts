import { Routes } from '@angular/router';

export const REFERENCE_DATA_COMPLETENESS_ROUTES: Routes = [
    {
        path: '',
        loadComponent: () =>
            import('./pages/reference-data-completeness-page/reference-data-completeness.page').then(
                (m) => m.ReferenceDataCompletenessPage
            )
    }
];
