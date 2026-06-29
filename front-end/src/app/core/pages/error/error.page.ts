import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';

@Component({
    selector: 'app-error',
    standalone: true,
    imports: [ButtonModule, RippleModule, RouterModule],
    templateUrl: './error.page.html',
    styleUrls: ['./error.page.scss']
})
export class ErrorPage {}
