import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';
import { provideRouter, withEnabledBlockingInitialNavigation, withInMemoryScrolling } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeuix/themes/aura';

import { APP_ROUTES } from './app.routes';
import { AppResponseInterceptor } from '@core/interceptors/app-response.interceptor';
import {AppAuthInterceptor} from "@core/interceptors/app-auth.interceptor";
import {ConfirmationService, MessageService} from "primeng/api";


export const appConfig: ApplicationConfig = {
    providers: [
        ConfirmationService,
        MessageService,
        provideHttpClient(
            withFetch(),
            withInterceptors([AppAuthInterceptor, AppResponseInterceptor])
        ),

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
