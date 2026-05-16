import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';

@Component({
    selector: 'app-access-denied',
    standalone: true,
    imports: [ButtonModule, RouterModule, RippleModule],
    templateUrl: './access-denied.page.html',
    styleUrls: ['./access-denied.page.scss']
})
export class AccessDeniedPage {}
