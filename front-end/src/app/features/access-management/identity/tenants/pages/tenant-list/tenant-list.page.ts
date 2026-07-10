import { Component, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';
import { TooltipModule } from 'primeng/tooltip';
import { forkJoin } from 'rxjs';
import { BaseListComponent } from '@shared/components/base/base-list.component';
import { ToolbarActionComponent } from '@shared/components/toolbar/toolbar-action.component';
import { TenantService } from '../../services/tenant.service';
import { Tenant } from '@features/access-management/models/tenant.model';
import { HasPermissionDirective } from '@shared/directives/permission/has-permission.directive';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
    selector: 'tenant-list',
    standalone: true,
    imports: [CommonModule, RouterModule, TableModule, ButtonModule, TagModule, InputTextModule, TooltipModule, ToolbarActionComponent, HasPermissionDirective],
    templateUrl: './tenant-list.page.html',
    providers: [ConfirmationService, MessageService]
})
export class TenantListPage extends BaseListComponent<Tenant> {
    protected override resourceName = 'tenant';

    private readonly service = inject(TenantService);
    private readonly router = inject(Router);
    private readonly confirmationService = inject(ConfirmationService);

    selectedTenants: Tenant[] = [];

    @ViewChild('dt') dt!: Table;

    override can(action: string): boolean {
        const normalizedAction = action.toLowerCase();

        if (normalizedAction === 'read' && this.permissionService.hasAnyRole(['SUPER_ADMIN', 'TENANT_ADMIN', 'IAM_ADMIN'])) {
            return true;
        }

        return super.can(action);
    }

    fetch() {
        return this.service.getAll();
    }

    openCreate() {
        void this.router.navigate(['/tenants/create']);
    }

    openDetail(tenant: Tenant) {
        if (!tenant.id) return;
        void this.router.navigate(['/tenants', tenant.id]);
    }

    openEdit(tenant: Tenant) {
        if (!tenant.id) return;
        void this.router.navigate(['/tenants', tenant.id, 'edit']);
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
        const requests = this.selectedTenants.map((tenant) => this.service.delete(tenant.id!));

        forkJoin(requests).subscribe(() => {
            this.refresh();
            this.selectedTenants = [];
        });
    }

    exportCSV() {
        this.dt.exportCSV();
    }

    onSearch(value: string) {
        this.dt.filterGlobal(value, 'contains');
    }

    displayValue(value?: string | null) {
        return value?.trim() || '-';
    }

    displayDate(value?: string | null) {
        if (!value) return '-';
        return new Date(value).toLocaleDateString();
    }

    getBooleanLabel(value?: boolean | null) {
        return value ? 'Yes' : 'No';
    }

    getBooleanSeverity(value?: boolean | null) {
        return value ? 'success' : 'secondary';
    }

    getStatusSeverity(status?: string | null) {
        switch (status) {
            case 'ACTIVE':
                return 'success';
            case 'SUSPENDED':
                return 'warn';
            case 'EXPIRED':
                return 'danger';
            default:
                return 'secondary';
        }
    }
}
