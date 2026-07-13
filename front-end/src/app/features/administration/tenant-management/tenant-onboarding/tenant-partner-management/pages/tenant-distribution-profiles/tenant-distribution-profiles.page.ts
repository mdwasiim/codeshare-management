import { Component, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';

import { BaseListComponent } from '@shared/components/base/base-list.component';
import { ToolbarActionComponent } from '@shared/components/toolbar/toolbar-action.component';
import { AppDialogComponent } from '@shared/components/app-dialog/app-dialog.component';
import { HasPermissionDirective } from '@shared/directives/permission/has-permission.directive';

import { AppConfirmService } from '@services/app-confirm.service';
import { AppToastService } from '@services/toast/app-toast.service';

import { TenantPartnerDistributionProfile } from '@features/administration/tenant-management/models/tenant-partner-profile.model';
import { TenantPartnerService } from '../../services/tenant-partner.service';
import { TenantPartnerDistributionProfileService } from '../../services/tenant-partner-distribution-profile.service';
import {
    TenantPartnerOption,
    formatEnumLabel,
    toTenantPartnerOptions
} from '../../shared/tenant-partner-profile.helpers';
import { TenantDistributionProfileFormPage } from '../tenant-distribution-profile-form/tenant-distribution-profile-form.page';

@Component({
    selector: 'tenant-distribution-profiles',
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
        TenantDistributionProfileFormPage
    ],
    templateUrl: './tenant-distribution-profiles.page.html'
})
export class TenantDistributionProfilesPage extends BaseListComponent<TenantPartnerDistributionProfile> {
    protected override resourceName = 'CODESHAREPARTNER';

    private readonly service = inject(TenantPartnerDistributionProfileService);
    private readonly tenantPartnerService = inject(TenantPartnerService);
    private readonly toast = inject(AppToastService);
    private readonly confirm = inject(AppConfirmService);

    dialogVisible = false;
    selectedId: number | null = null;
    selectedProfiles: TenantPartnerDistributionProfile[] = [];
    partnerOptions: TenantPartnerOption[] = [];

    @ViewChild('dt') private dt?: Table;

    override ngOnInit(): void {
        super.ngOnInit();
        this.loadPartnerOptions();
    }

    override fetch() {
        return this.service.getAll();
    }

    openCreate(): void {
        this.selectedId = null;
        this.dialogVisible = true;
    }

    openEdit(profile: TenantPartnerDistributionProfile): void {
        this.selectedId = profile.id ?? null;
        this.dialogVisible = true;
    }

    deleteProfile(profile: TenantPartnerDistributionProfile): void {
        if (!profile.id) {
            return;
        }

        this.confirm.delete(`Delete distribution profile "${profile.profileName || profile.profileCode}"?`, () => {
            this.service.delete(profile.id!).subscribe({
                next: () => this.refresh(),
                error: () => this.toast.error('Failed to delete distribution profile')
            });
        });
    }

    deleteSelected(): void {
        if (!this.selectedProfiles.length) {
            return;
        }

        const requests = this.selectedProfiles
            .filter((profile) => !!profile.id)
            .map((profile) => this.service.delete(profile.id!));

        this.confirm.delete(`Delete ${requests.length} selected profile(s)?`, () => {
            forkJoin(requests).subscribe({
                next: () => {
                    this.selectedProfiles = [];
                    this.refresh();
                },
                error: () => this.toast.error('Failed to delete selected distribution profiles')
            });
        });
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

    partnerLabel(partnerId?: number): string {
        return this.partnerOptions.find((option) => option.value === partnerId)?.label || (partnerId != null ? String(partnerId) : '-');
    }

    formatLabel(value?: string | null): string {
        return formatEnumLabel(value);
    }

    private loadPartnerOptions(): void {
        this.tenantPartnerService.getAll().subscribe((partners) => {
            this.partnerOptions = toTenantPartnerOptions(partners ?? []);
        });
    }
}
