import { Component, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';

import { forkJoin } from 'rxjs';
import { BaseListComponent } from '@shared/components/base/base-list.component';

import { ToolbarActionComponent } from '@shared/components/toolbar/toolbar-action.component';
import { TenantService } from '../../services/tenant.service';
import { TenantFormPage } from '@features/access-management/iam/tenants/pages/tenant-form/tenant-form.page';
import { Tenant } from '@features/access-management/iam/models/tenant.model';
import { TooltipModule } from 'primeng/tooltip';
import { CsmDialogComponent } from '@shared/components/csm-dialog/csm-dialog.component';
import { HasPermissionDirective } from '@shared/directives/permission/has-permission.directive';

@Component({
    selector: 'tenant-list',
    standalone: true,
    imports: [CommonModule, TableModule, ButtonModule, TagModule, InputTextModule, ConfirmDialogModule, ToastModule, ToolbarActionComponent, TenantFormPage, TooltipModule, TenantFormPage, CsmDialogComponent, HasPermissionDirective],
    templateUrl: './tenant-list.page.html',
    providers: [ConfirmationService, MessageService]
})
export class TenantListPage extends BaseListComponent<Tenant> {
    protected override resourceName = 'tenants';
    dialogVisible = false;
    selectedId: string | null = null;

    private service = inject(TenantService);
    private confirmationService = inject(ConfirmationService);

    selectedTenants: Tenant[] = [];

    @ViewChild('dt') dt!: Table;

    fetch() {
        return this.service.getAll();
    }

    openCreate() {
        this.selectedId = null;
        this.dialogVisible = true;
    }

    openEdit(tenant: Tenant) {
        this.selectedId = tenant.id ?? null;
        this.dialogVisible = true;
    }

    deleteTenant(tenant: Tenant) {
        this.confirmationService.confirm({
            message: `Delete tenant "${tenant.name}"?`,
            accept: () => {
                this.service.delete(tenant.id!).subscribe(() => this.refresh());
            }
        });
    }

    deleteSelectedTenants() {
        const requests = this.selectedTenants.map((t) => this.service.delete(t.id!));

        forkJoin(requests).subscribe(() => {
            this.refresh();
            this.selectedTenants = [];
        });
    }

    exportCSV() {
        this.dt.exportCSV();
    }

    onSaved() {
        this.refresh();
    }

    onSearch(value: string) {
        this.dt.filterGlobal(value, 'contains');
    }

    getStatusSeverity(status: string): 'success' | 'secondary' | 'danger' | 'info' | 'warn' {
        switch (status) {
            case 'ACTIVE':
                return 'success';
            case 'INACTIVE':
                return 'secondary';
            case 'SUSPENDED':
                return 'warn';
            case 'EXPIRED':
                return 'danger';
            default:
                return 'info';
        }
    }
}
