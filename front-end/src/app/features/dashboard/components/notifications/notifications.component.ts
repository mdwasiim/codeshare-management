import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';

@Component({
    standalone: true,
    selector: 'notifications-widget',
    imports: [ButtonModule, MenuModule],
    templateUrl: './notifications-widget.component.html',
    styleUrls: ['./notifications-widget.component.scss']
})
export class NotificationsWidgetComponent {
    items = [
        { label: 'Add New', icon: 'pi pi-fw pi-plus' },
        { label: 'Remove', icon: 'pi pi-fw pi-trash' }
    ];
}
