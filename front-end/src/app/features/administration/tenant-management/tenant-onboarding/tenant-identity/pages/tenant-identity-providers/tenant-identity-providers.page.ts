import { Component, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';

import { BaseListComponent } from '@shared/components/base/base-list.component';
import { ToolbarActionComponent } from '@shared/components/toolbar/toolbar-action.component';
import { AppDialogComponent } from '@shared/components/app-dialog/app-dialog.component';
import { HasPermissionDirective } from '@shared/directives/permission/has-permission.directive';

import { TenantIdentityProviderRow, TenantStatus } from '@features/administration/tenant-management/models/tenant.model';
import { TenantService } from '@features/administration/tenant-management/tenant-onboarding/tenant-administration/tenants/services/tenant.service';
import { TenantFormPage } from '@features/administration/tenant-management/tenant-onboarding/tenant-administration/tenants/pages/tenant-form/tenant-form.page';

@Component({
    selector: 'tenant-identity-providers',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        TagModule,
        TooltipModule,
        ToolbarActionComponent,
        AppDialogComponent,
        HasPermissionDirective,
        TenantFormPage
    ],
    templateUrl: './tenant-identity-providers.page.html'
})
export class TenantIdentityProvidersPage extends BaseListComponent<TenantIdentityProviderRow> {
    protected override resourceName = 'TENANT';

    private readonly service = inject(TenantService);

    dialogVisible = false;
    selectedTenantId: number | null = null;

    @ViewChild('dt') private dt?: Table;

    override fetch() {
        return this.service.getIdentityProviders(this.exactFilters);
    }

    openEdit(tenant: TenantIdentityProviderRow): void {
        this.selectedTenantId = tenant.id ?? null;
        this.dialogVisible = true;
    }

    onSaved(): void {
        this.dialogVisible = false;
        this.refresh();
    }

    onSearch(value: string): void {
        this.dt?.filterGlobal(value, 'contains');
    }

    exportCsv(): void {
        this.dt?.exportCSV();
    }

    authSourceLabel(tenant: TenantIdentityProviderRow): string {
        return this.formatAuthSource(tenant.authSource) || 'Internal';
    }

    hasExternalProvider(tenant: TenantIdentityProviderRow): boolean {
        return !!tenant.authSource && tenant.authSource !== 'INTERNAL';
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

    private formatAuthSource(authSource?: string): string {
        return authSource
            ? authSource
                  .toLowerCase()
                  .split('_')
                  .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
                  .join(' ')
            : '';
    }
}
