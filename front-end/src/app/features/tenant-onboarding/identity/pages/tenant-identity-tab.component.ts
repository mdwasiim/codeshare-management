import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { TenantService } from '@features/access-management/identity/tenants/services/tenant.service';
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
    mode: 'identity-providers' | 'oidc-config' = 'identity-providers';

    constructor() {
        this.mode = (this.route.snapshot.routeConfig?.path as 'identity-providers' | 'oidc-config') || 'identity-providers';

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

    get title() {
        return this.mode === 'oidc-config' ? 'OIDC Configuration' : 'Identity Providers';
    }

    get description() {
        return this.mode === 'oidc-config'
            ? 'Issuer, client, redirect, token, and scope settings for tenant authentication.'
            : 'Authentication source order and provider enablement for this tenant.';
    }

    openEdit() {
        if (!this.tenant?.id) return;
        void this.router.navigate(['/tenants', this.tenant.id, 'edit']);
    }

    getAuthSourceLabel(authSource: AuthSource) {
        return authSource.replaceAll('_', ' ');
    }
}
