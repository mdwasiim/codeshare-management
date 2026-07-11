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

import { TenantPartnerProfile } from '@features/administration/tenant-management/models/tenant-partner-profile.model';
import { TenantPartnerProfileService } from '../../services/tenant-partner-profile.service';
import { TenantPartnerService } from '../../services/tenant-partner.service';
import {
    TenantPartnerOption,
    formatEnumLabel,
    toTenantPartnerOptions
} from '../../shared/tenant-partner-profile.helpers';
import { TenantPartnerProfileFormPage } from '../tenant-partner-profile-form/tenant-partner-profile-form.page';

@Component({
    selector: 'tenant-partner-profiles',
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
        TenantPartnerProfileFormPage
    ],
    templateUrl: './tenant-partner-profiles.page.html'
})
export class TenantPartnerProfilesPage extends BaseListComponent<TenantPartnerProfile> {
    protected override resourceName = 'CODESHAREPARTNER';

    private readonly service = inject(TenantPartnerProfileService);
    private readonly tenantPartnerService = inject(TenantPartnerService);
    private readonly toast = inject(AppToastService);
    private readonly confirm = inject(AppConfirmService);

    dialogVisible = false;
    selectedId: string | null = null;
    selectedProfiles: TenantPartnerProfile[] = [];
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

    openEdit(profile: TenantPartnerProfile): void {
        this.selectedId = profile.id ?? null;
        this.dialogVisible = true;
    }

    deleteProfile(profile: TenantPartnerProfile): void {
        if (!profile.id) {
            return;
        }

        this.confirm.delete(`Delete partner profile "${profile.profileName || profile.profileCode}"?`, () => {
            this.service.delete(profile.id!).subscribe({
                next: () => this.refresh(),
                error: () => this.toast.error('Failed to delete partner profile')
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
                error: () => this.toast.error('Failed to delete selected partner profiles')
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

    partnerLabel(partnerId?: string): string {
        return this.partnerOptions.find((option) => option.value === partnerId)?.label || partnerId || '-';
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
