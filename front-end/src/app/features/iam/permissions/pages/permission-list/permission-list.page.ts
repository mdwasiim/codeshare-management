import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { Permission } from '@features/iam/models/permission.model';
import { BaseListComponent } from '@core/base/base-list.component';
import {PermissionService} from "@features/iam/permissions/services/permission.service";

@Component({
    selector: 'app-permission-list',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './permission-list.component.html'
})
export class PermissionListPage extends BaseListComponent<Permission> {

    private service = inject(PermissionService);
    private router = inject(Router);

    fetch() {
        return this.service.getAll();
    }

    createPermission() {
        this.router.navigate(['/iam/permissions/create']);
    }

    editPermission(permission: Permission) {
        this.router.navigate(['/iam/permissions', permission.id]);
    }

    deletePermission(permission: Permission) {
        if (!confirm(`Delete "${permission.name}"?`)) return;

        this.service.delete(permission.id!).subscribe(() => this.loadData());
    }
}
