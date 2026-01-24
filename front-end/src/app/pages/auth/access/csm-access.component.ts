import { CSMFloatingConfigurator } from '@/layout/floating-configurator/csm.floating-configurator';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';

@Component({
    selector: 'csm-access',
    standalone: true,
    imports: [
        ButtonModule,
        RouterModule,
        RippleModule,
        ButtonModule
    ],
    templateUrl: './csm-access.component.html',
    styleUrls: ['./csm-access.component.scss']
})
export class CSMAccess {}
