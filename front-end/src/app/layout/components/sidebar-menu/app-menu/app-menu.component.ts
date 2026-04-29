import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LayoutMenuService } from '@shared/services/layout-menu.service';
import {AppMenuItemComponent} from "@layout/components/sidebar-menu/app-menu-item/app-menu-item.component";
import {AppMenuModel} from "@shared/models/app-menu.model";

@Component({
    selector: 'app-menu',
    standalone: true,
    imports: [AppMenuItemComponent, RouterModule],
    templateUrl: './app-menu.component.html',
    styleUrls: ['./app-menu.component.scss']
})
export class AppMenuComponent implements OnInit {

    model: AppMenuModel[] = [];

    constructor(private menuService: LayoutMenuService) {}

    ngOnInit(): void {
        // 🔥 trigger load (once)
        this.menuService.loadMenus().subscribe();

        // 🔥 always listen to state
        this.menuService.getMenu().subscribe({
            next: (menu) => (this.model = menu),
            error: (err) => console.error('Menu stream error', err)
        });
    }
}
