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
    templateUrl: './access-denied.component.html',
    styleUrls: ['./access-denied.component.scss']
})
export class AccessDeniedComponent {}
