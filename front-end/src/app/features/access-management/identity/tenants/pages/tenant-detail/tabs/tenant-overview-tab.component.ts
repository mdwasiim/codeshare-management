import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { TenantService } from '../../../services/tenant.service';
import { Tenant } from '@features/access-management/models/tenant.model';
import { TagModule } from 'primeng/tag';

@Component({
    selector: 'tenant-overview-tab',
    standalone: true,
    imports: [CommonModule, TagModule],
    templateUrl: './tenant-overview-tab.component.html'
})
export class TenantOverviewTabComponent {
    private readonly route = inject(ActivatedRoute);
    private readonly tenantService = inject(TenantService);

    tenant: Tenant | null = null;

    constructor() {
        this.route.parent?.paramMap
            .pipe(
                switchMap((params) => {
                    const id = params.get('id');
                    return id ? this.tenantService.getById(id) : of(null);
                })
            )
            .subscribe((tenant) => (this.tenant = tenant));
    }
}
