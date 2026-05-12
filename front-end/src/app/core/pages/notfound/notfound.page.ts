import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'app-notfound',
    standalone: true,
    imports: [CommonModule, ButtonModule],
    templateUrl: './notfound.page.html'
})
export class NotfoundPage {
    constructor(private router: Router) {}

    goHome(): void {
        this.router.navigate(['/']);
    }
}
