import { provideHttpClient, withFetch } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideRouter, withEnabledBlockingInitialNavigation, withInMemoryScrolling } from '@angular/router';
import Aura from '@primeuix/themes/aura';
import { providePrimeNG } from 'primeng/config';

import { csmRoutes } from './csm.routes';

export const csmConfig: ApplicationConfig = {
    providers: [
        provideRouter(
            csmRoutes,
            withInMemoryScrolling({ anchorScrolling: 'enabled', scrollPositionRestoration: 'enabled' }),
            withEnabledBlockingInitialNavigation()
        ),
        provideHttpClient(withFetch()),
        provideAnimationsAsync(),
        providePrimeNG({
            theme: {
                preset: Aura,
                options: {
                    darkModeSelector: '.csm-dark'   // ← rename if you changed dark class
                }
            }
        })
    ]
};
