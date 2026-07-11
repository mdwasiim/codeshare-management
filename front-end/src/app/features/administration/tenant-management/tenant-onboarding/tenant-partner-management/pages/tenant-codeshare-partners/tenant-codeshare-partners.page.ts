import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';

import { BaseListComponent } from '@shared/components/base/base-list.component';
import { ToolbarActionComponent } from '@shared/components/toolbar/toolbar-action.component';
import { TenantPartnerService } from '../../services/tenant-partner.service';
import { TenantPartner } from '@features/administration/tenant-management/models/tenant-partner.model';

@Component({
    selector: 'tenant-codeshare-partners',
    standalone: true,
    imports: [CommonModule, TableModule, ButtonModule, TagModule, ToolbarActionComponent],
    templateUrl: './tenant-codeshare-partners.page.html'
})
export class TenantCodesharePartnersPage extends BaseListComponent<TenantPartner> {
    protected override resourceName = 'CODESHARE_PARTNER';
    private readonly service = inject(TenantPartnerService);

    override fetch() {
        return this.service.getAll();
    }
}
