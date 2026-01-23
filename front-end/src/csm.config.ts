import { provideHttpClient, withFetch } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';
import { provideRouter, withEnabledBlockingInitialNavigation, withInMemoryScrolling } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import Aura from '@primeuix/themes/aura';
import { providePrimeNG } from 'primeng/config';

import { csmRoutes } from './csm.routes';

export const csmConfig: ApplicationConfig = {
    providers: [
        provideRouter(
            csmRoutes,
            withInMemoryScrolling({
                anchorScrolling: 'enabled',
                scrollPositionRestoration: 'enabled'
            }),
            withEnabledBlockingInitialNavigation()
        ),
        provideHttpClient(withFetch()),
        provideAnimations(),   // ✅ FIXED
        providePrimeNG({
            theme: {
                preset: Aura,
                options: {
                    darkModeSelector: '.csm-dark'
                }
            }
        })
    ]
};
