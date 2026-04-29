import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AppMenuComponent } from '@layout/components/menu/app-menu-item/app-menu.component';
import { MenuItemModel } from '@features/iam/menu/models/menu-item.model';
import { MenuService } from '@layout/services/menu.service';

@Component({
    selector: 'app-menu',
    standalone: true,
    imports: [AppMenuComponent, RouterModule],
    templateUrl: './app-menu-item.component.html',
    styleUrls: ['./app-menu-item.component.scss']
})
export class AppMenuItemComponent implements OnInit {

    model: MenuItemModel[] = [];

    constructor(private menuService: MenuService) {}

    ngOnInit(): void {
        this.menuService.loadMenus().subscribe({
            next: (menu) => (this.model = menu),
            error: (err) => console.error('Menu load failed', err)
        });
    }
}
