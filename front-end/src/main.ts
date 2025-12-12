import { provideZoneChangeDetection } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { CsmComponent } from './csm.component';
import { csmConfig } from './csm.config';


bootstrapApplication(CsmComponent, {
    ...csmConfig,
    providers: [
        provideZoneChangeDetection(),
        ...csmConfig.providers
    ]
}).catch(err => console.error(err));
