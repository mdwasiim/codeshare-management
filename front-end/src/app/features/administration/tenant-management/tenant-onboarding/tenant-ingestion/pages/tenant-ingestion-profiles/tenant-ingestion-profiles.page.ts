import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';

import { BaseListComponent } from '@shared/components/base/base-list.component';
import { ToolbarActionComponent } from '@shared/components/toolbar/toolbar-action.component';
import { HasPermissionDirective } from '@shared/directives/permission/has-permission.directive';

import { TenantIngestionProfile } from '@features/administration/tenant-management/models/tenant-ingestion-profile.model';
import { TenantIngestionProfileService } from '../../services/tenant-ingestion-profile.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AppConfirmService } from '@core/services/app-confirm.service';

@Component({
    selector: 'tenant-ingestion-profiles',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        TagModule,
        TooltipModule,
        ToolbarActionComponent,
        HasPermissionDirective
    ],
    templateUrl: './tenant-ingestion-profiles.page.html'
})
export class TenantIngestionProfilesPage extends BaseListComponent<TenantIngestionProfile> {
    protected override resourceName = 'TENANT_INGESTION_PROFILE';

    private readonly service = inject(TenantIngestionProfileService);
    private readonly toast = inject(AppToastService);
    private readonly confirm = inject(AppConfirmService);

    override fetch() {
        return this.service.getAll();
    }

    toggleEnable(profile: TenantIngestionProfile): void {
        const enabled = !profile.enabled;
        this.service.enable(profile.id!, enabled).subscribe({
            next: () => {
                this.toast.success(`Profile ${enabled ? 'enabled' : 'disabled'}`);
                this.refresh();
            },
            error: () => this.toast.error('Failed to update profile status')
        });
    }

    deleteProfile(profile: TenantIngestionProfile): void {
        if (!profile.id) {
            return;
        }

        this.confirm.delete(`Delete ingestion profile for "${profile.tenantCode || profile.airlineCode}"?`, () => {
            this.service.delete(profile.id!).subscribe({
                next: () => {
                    this.toast.success('Profile deleted');
                    this.refresh();
                },
                error: () => this.toast.error('Failed to delete profile')
            });
        });
    }
}
