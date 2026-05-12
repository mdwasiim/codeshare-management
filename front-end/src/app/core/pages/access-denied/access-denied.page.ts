import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'app-access-denied',
    standalone: true,
    imports: [CommonModule, ButtonModule],
    templateUrl: './access-denied.page.html'
})
export class AccessDeniedPage {
    constructor(private router: Router) {}

    goHome(): void {
        this.router.navigate(['/']);
    }
}
