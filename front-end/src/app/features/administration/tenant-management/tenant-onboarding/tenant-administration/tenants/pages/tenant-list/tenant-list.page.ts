import { Component, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';
import { TooltipModule } from 'primeng/tooltip';

import { BaseListComponent } from '@shared/components/base/base-list.component';
import { ToolbarActionComponent } from '@shared/components/toolbar/toolbar-action.component';
import { AppDialogComponent } from '@shared/components/app-dialog/app-dialog.component';
import { HasPermissionDirective } from '@shared/directives/permission/has-permission.directive';

import { Tenant, TenantStatus } from '@features/administration/tenant-management/models/tenant.model';
import { TenantService } from '../../services/tenant.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AppConfirmService } from '@core/services/app-confirm.service';
import { TenantFormPage } from '../tenant-form/tenant-form.page';

@Component({
    selector: 'tenant-list',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        TagModule,
        InputTextModule,
        TooltipModule,
        ToolbarActionComponent,
        AppDialogComponent,
        HasPermissionDirective,
        TenantFormPage
    ],
    templateUrl: './tenant-list.page.html'
})
export class TenantListPage extends BaseListComponent<Tenant> {
    protected override resourceName = 'TENANT';

    private readonly service = inject(TenantService);
    private readonly toast = inject(AppToastService);
    private readonly confirm = inject(AppConfirmService);

    dialogVisible = false;
    selectedTenantId: string | null = null;
    selectedTenants: Tenant[] = [];

    @ViewChild('dt') private dt?: Table;

    override fetch() {
        return this.service.getAll();
    }

    openCreate(): void {
        this.selectedTenantId = null;
        this.dialogVisible = true;
    }

    openEdit(tenant: Tenant): void {
        this.selectedTenantId = tenant.id ?? null;
        this.dialogVisible = true;
    }

    deleteTenant(tenant: Tenant): void {
        if (!tenant.id) {
            return;
        }

        this.confirm.delete(`Delete tenant "${tenant.name}"?`, () => {
            this.service.delete(tenant.id!).subscribe({
                next: () => this.refresh(),
                error: () => this.toast.error('Failed to delete tenant')
            });
        });
    }

    deleteSelected(): void {
        if (!this.selectedTenants.length) {
            return;
        }

        this.confirm.delete(`Delete ${this.selectedTenants.length} selected tenant(s)?`, () => {
            const requests = this.selectedTenants
                .filter((tenant) => !!tenant.id)
                .map((tenant) => this.service.delete(tenant.id!));

            forkJoin(requests).subscribe({
                next: () => {
                    this.toast.success('Tenants deleted successfully');
                    this.selectedTenants = [];
                    this.refresh();
                },
                error: () => this.toast.error('Failed to delete tenants')
            });
        });
    }

    exportCsv(): void {
        this.dt?.exportCSV();
    }

    onSaved(): void {
        this.dialogVisible = false;
        this.refresh();
    }

    onSearch(value: string): void {
        this.dt?.filterGlobal(value, 'contains');
    }

    statusSeverity(status?: TenantStatus): 'success' | 'warn' | 'danger' | 'secondary' {
        switch (status) {
            case 'ACTIVE':
                return 'success';
            case 'SUSPENDED':
                return 'warn';
            case 'EXPIRED':
            case 'DELETED':
                return 'danger';
            default:
                return 'secondary';
        }
    }
}
