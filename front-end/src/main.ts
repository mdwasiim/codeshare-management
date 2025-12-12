import { provideZoneChangeDetection } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { CSMComponent } from './csm.component';
import { csmConfig } from './csm.config';


bootstrapApplication(CSMComponent, {
    ...csmConfig,
    providers: [
        provideZoneChangeDetection(),
        ...csmConfig.providers
    ]
}).catch(err => console.error(err));
