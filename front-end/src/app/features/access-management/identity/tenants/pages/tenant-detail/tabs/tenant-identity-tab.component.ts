import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { TenantService } from '../../../services/tenant.service';
import { AuthSource, IdentityProviderConfig, Tenant } from '@features/access-management/models/tenant.model';

@Component({
    selector: 'tenant-identity-tab',
    standalone: true,
    imports: [CommonModule, RouterModule, ButtonModule, TableModule, TagModule],
    templateUrl: './tenant-identity-tab.component.html'
})
export class TenantIdentityTabComponent {
    private readonly route = inject(ActivatedRoute);
    private readonly router = inject(Router);
    private readonly tenantService = inject(TenantService);

    tenant: Tenant | null = null;
    providers: IdentityProviderConfig[] = [];

    constructor() {
        this.route.parent?.paramMap
            .pipe(
                switchMap((params) => {
                    const id = params.get('id');
                    return id ? this.tenantService.getById(id) : of(null);
                })
            )
            .subscribe((tenant) => {
                this.tenant = tenant;
                if (!tenant?.tenantCode) {
                    this.providers = [];
                    return;
                }

                this.tenantService.getAuthContextByCode(tenant.tenantCode).subscribe((context) => {
                    if (context.identityProviders?.length) {
                        this.providers = context.identityProviders;
                        return;
                    }

                    if (tenant.authSource) {
                        this.providers = [
                            {
                                providerId: tenant.id || tenant.tenantCode,
                                authSource: tenant.authSource,
                                enabled: true,
                                priority: 1,
                                oidcConfig: tenant.oidcConfig
                            }
                        ];
                    }
                });
            });
    }

    openEdit() {
        if (!this.tenant?.id) return;
        void this.router.navigate(['/tenants', this.tenant.id, 'edit']);
    }

    getAuthSourceLabel(authSource: AuthSource) {
        return authSource.replaceAll('_', ' ');
    }
}
