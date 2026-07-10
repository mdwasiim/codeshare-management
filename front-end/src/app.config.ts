import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { ApplicationConfig, inject, provideAppInitializer } from '@angular/core';
import { provideRouter, withEnabledBlockingInitialNavigation, withInMemoryScrolling } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeuix/themes/aura';

import { APP_ROUTES } from './app.routes';
import { AppResponseInterceptor } from '@core/security/interceptors/app-response.interceptor';
import { AppAuthInterceptor } from '@core/security/interceptors/app-auth.interceptor';
import { AppSpinnerInterceptor } from '@core/security/interceptors/app-spinner.interceptor';
import { ConfirmationService, MessageService } from 'primeng/api';
import { AuthInitializerService } from '@services/auth/auth-initializer.service';

export const appConfig: ApplicationConfig = {
    providers: [
        ConfirmationService,
        MessageService,
        provideAppInitializer(() => inject(AuthInitializerService).init()),
        provideHttpClient(withFetch(), withInterceptors([AppSpinnerInterceptor, AppAuthInterceptor, AppResponseInterceptor])),

        provideRouter(
            APP_ROUTES,
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
                    darkModeSelector: '.app-dark'
                }
            }
        })
    ]
};
