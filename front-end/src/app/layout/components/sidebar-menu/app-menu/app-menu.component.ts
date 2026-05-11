import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import {AppMenuItemComponent} from "@layout/components/sidebar-menu/app-menu-item/app-menu-item.component";
import {AppMenuModel} from "@features/access-management/iam/models/app-menu.model";
import {LayoutMenuService} from "@layout/services/layout-menu.service";
import {MenuItem} from "primeng/api";

@Component({
    selector: 'app-menu',
    standalone: true,
    imports: [AppMenuItemComponent, RouterModule],
    templateUrl: './app-menu.component.html',
    styleUrls: ['./app-menu.component.scss']
})
export class AppMenuComponent implements OnInit {

    model: AppMenuModel[] = [];
    //model: MenuItem[] = [];

    constructor(private menuService: LayoutMenuService) {}

    ngOnInit(): void {

        this.menuService.loadMenus().subscribe();

        this.menuService.getMenu().subscribe(menu => {
            if (!menu.length) return;

            // ensure root is selected
            let root = this.menuService['selectedRootMenuSubject'].value;

            if (!root) {
                root = menu[0];
                this.menuService.setSelectedRoot(root);
            }

            this.model = root?.items || [];
        });

        this.menuService.selectedRootMenu$.subscribe(root => {
            this.model = root?.items || [];
        });
    }
}
