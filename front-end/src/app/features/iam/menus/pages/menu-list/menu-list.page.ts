import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';
import { BaseListComponent } from '@core/base/base-list.component';
import { MenuManagementService } from '../../services/menu-management.service';
import { AppMenuModel } from '@shared/models/app-menu.model';

@Component({
    selector: 'menu-list',
    standalone: true,
    imports: [CommonModule, TableModule, ButtonModule],
    templateUrl: './menu-list.page.html'
})
export class MenuListPage extends BaseListComponent<AppMenuModel> implements OnInit {

    private service = inject(MenuManagementService);
    private router = inject(Router);

    ngOnInit(): void {
        this.loadData();
    }

    fetch() {
        return this.service.getAll();
    }

    createMenu() {
        this.router.navigate(['/iam/menu/create']);
    }

    editMenu(menu: AppMenuModel) {
        this.router.navigate(['/iam/menu', menu.id]);
    }

    deleteMenu(menu: AppMenuModel) {
        if (!confirm(`Delete menu "${menu.label}"?`)) return;

        this.service.delete(menu.id!).subscribe(() => this.loadData());
    }
}
