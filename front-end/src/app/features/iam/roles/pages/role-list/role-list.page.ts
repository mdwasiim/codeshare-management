import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';

import { Role } from '@features/iam/models/role.model';
import { BaseListComponent } from '@core/base/base-list.component';
import { RoleService } from '../../services/role.service';

@Component({
    selector: 'role-list',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        TagModule
    ],
    templateUrl: './role-list.page.html'
})
export class RoleListPage extends BaseListComponent<Role> {

    private service = inject(RoleService);
    private router = inject(Router);

    tenantId = 'QR';

    fetch() {
        return this.service.getAll(this.tenantId);
    }

    createRole() {
        this.router.navigate(['/iam/roles/create']);
    }

    editRole(role: Role) {
        this.router.navigate(['/iam/roles', role.id]);
    }

    deleteRole(role: Role) {
        if (!confirm(`Delete role "${role.name}"?`)) return;

        this.service.delete(role.id!).subscribe(() => this.loadData());
    }
}
