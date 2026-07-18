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

import { TenantOidcConfigRow } from '@features/administration/tenant-management/models/tenant.model';
import { TenantService } from '@features/administration/tenant-management/tenant-onboarding/tenant-administration/tenants/services/tenant.service';
import { TenantFormPage } from '@features/administration/tenant-management/tenant-onboarding/tenant-administration/tenants/pages/tenant-form/tenant-form.page';

@Component({
    selector: 'tenant-oidc-config',
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
    templateUrl: './tenant-oidc-config.page.html'
})
export class TenantOidcConfigPage extends BaseListComponent<TenantOidcConfigRow> {
    protected override resourceName = 'TENANT';

    private readonly service = inject(TenantService);

    dialogVisible = false;
    selectedTenantId: number | null = null;

    @ViewChild('dt') private dt?: Table;

    override fetch() {
        return this.service.getOidcConfigs(this.exactFilters);
    }

    openEdit(tenant: TenantOidcConfigRow): void {
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

    authSourceLabel(tenant: TenantOidcConfigRow): string {
        return tenant.authSource
            ? tenant.authSource
                  .toLowerCase()
                  .split('_')
                  .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
                  .join(' ')
            : 'Internal';
    }

    hasExternalProvider(tenant: TenantOidcConfigRow): boolean {
        return !!tenant.authSource && tenant.authSource !== 'INTERNAL';
    }
}
