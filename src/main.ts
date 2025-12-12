import { provideZoneChangeDetection } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';

import { csmConfig } from './csm.config';
import { CsmComponent } from './csm.component';

bootstrapApplication(CsmComponent, {
    ...csmConfig,
    providers: [
        provideZoneChangeDetection(),
        ...csmConfig.providers
    ]
}).catch(err => console.error(err));
