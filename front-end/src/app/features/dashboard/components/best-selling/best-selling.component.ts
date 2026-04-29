import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'best-selling',
    standalone: true,
    imports: [CommonModule, ButtonModule, MenuModule],
    templateUrl: './best-selling.component.html',
    styleUrls: ['./best-selling.component.scss']
})
export class BestSellingComponent {
    menu = null;

    items = [
        { label: 'Add New', icon: 'pi pi-fw pi-plus' },
        { label: 'Remove', icon: 'pi pi-fw pi-trash' }
    ];
}
