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
        ButtonModule
    ],
    templateUrl: './error.component.html',
    styleUrls: ['./error.component.scss']
})
export class ErrorComponent {}
