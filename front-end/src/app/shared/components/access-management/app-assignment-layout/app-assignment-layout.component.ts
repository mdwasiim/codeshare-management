import { Component, Input } from '@angular/core';

import { CommonModule } from '@angular/common';

import { CardModule } from 'primeng/card';

@Component({
    selector: 'app-assignment-layout',
    standalone: true,
    imports: [CommonModule, CardModule],
    templateUrl: './app-assignment-layout.component.html'
})
export class AppAssignmentLayoutComponent {
    @Input()
    groupTitle?: string;

    @Input()
    title = 'Manage Assignment';

    @Input()
    leftTitle?: string;

    @Input()
    rightTitle?: string;

    get layoutClass(): string {
        if (this.groupTitle && this.leftTitle && this.rightTitle) {
            return 'assignment-layout__grid assignment-layout__grid--three';
        }

        return 'assignment-layout__grid assignment-layout__grid--two';
    }
}
