import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'app-error',
    standalone: true,
    imports: [CommonModule, ButtonModule],
    templateUrl: './error.page.html'
})
export class ErrorPage {
    constructor(private router: Router) {}

    goHome(): void {
        this.router.navigate(['/']);
    }
}
