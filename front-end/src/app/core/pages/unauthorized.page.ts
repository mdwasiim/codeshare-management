import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'app-unauthorized',
    standalone: true,
    imports: [CommonModule, ButtonModule],
    templateUrl: './unauthorized.page.html'
})
export class UnauthorizedPage {

    constructor(private router: Router) {}

    goHome() {
        this.router.navigate(['/']);
    }
}
