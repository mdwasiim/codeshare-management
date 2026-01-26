import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';
import { provideRouter, withEnabledBlockingInitialNavigation, withInMemoryScrolling } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeuix/themes/aura';

import { csmRoutes } from './csm.routes';
import { AuthInterceptor } from '@/core/interceptors/auth.interceptor';
import { CSMResponseInterceptor } from '@/core/resolver/csm-response.interceptor';


export const csmConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(
         withFetch(),
        withInterceptors([AuthInterceptor, CSMResponseInterceptor])
    ),

    provideRouter(
      csmRoutes,
      withInMemoryScrolling({
        anchorScrolling: 'enabled',
        scrollPositionRestoration: 'enabled'
      }),
      withEnabledBlockingInitialNavigation()
    ),

    provideAnimations(),

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
