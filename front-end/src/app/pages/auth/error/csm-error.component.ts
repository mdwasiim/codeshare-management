import { CSMFloatingConfigurator } from '@/layout/floating-configurator/csm.floating-configurator';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';

@Component({
    selector: 'csm-error',
    standalone: true,
    imports: [
        ButtonModule,
        RippleModule,
        RouterModule,
        CSMFloatingConfigurator,
        ButtonModule
    ],
    templateUrl: './csm-error.component.html',
    styleUrls: ['./csm-error.component.scss']
})
export class CSMError {}
