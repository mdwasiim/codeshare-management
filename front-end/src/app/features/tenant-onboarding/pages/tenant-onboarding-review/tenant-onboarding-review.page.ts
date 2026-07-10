import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { TagModule } from 'primeng/tag';
import { TenantService } from '@features/access-management/identity/tenants/services/tenant.service';
import { Tenant } from '@features/access-management/models/tenant.model';
import { TenantIngestionProfileService } from '@features/access-management/identity/tenants/services/tenant-ingestion-profile.service';
import { TenantPartnerService } from '@features/partner-management/tenant-partners/services/tenant-partners.service';

@Component({
    selector: 'tenant-onboarding-review-page',
    standalone: true,
    imports: [CommonModule, TagModule],
    templateUrl: './tenant-onboarding-review.page.html'
})
export class TenantOnboardingReviewPage {
    private readonly route = inject(ActivatedRoute);
    private readonly tenantService = inject(TenantService);
    private readonly ingestionService = inject(TenantIngestionProfileService);
    private readonly partnerService = inject(TenantPartnerService);

    tenant: Tenant | null = null;
    checks: { label: string; complete: boolean; detail: string }[] = [];

    constructor() {
        this.route.parent?.paramMap
            .pipe(
                switchMap((params) => {
                    const id = params.get('id');
                    return id ? this.tenantService.getById(id) : of(null);
                }),
                switchMap((tenant) => {
                    this.tenant = tenant;
                    if (!tenant?.tenantCode) {
                        return of(null);
                    }

                    return forkJoin({
                        authContext: this.tenantService.getAuthContextByCode(tenant.tenantCode),
                        ingestionProfile: this.ingestionService.getByTenantCode(tenant.tenantCode),
                        partners: this.partnerService.getAll()
                    });
                })
            )
            .subscribe((result) => {
                const partners = (result?.partners ?? []).filter((partner) => partner.homeAirlineCode === this.tenant?.tenantCode);
                const providerCount = result?.authContext?.identityProviders?.length ?? (this.tenant?.authSource ? 1 : 0);
                const channelCount = result?.ingestionProfile?.channels?.length ?? 0;

                this.checks = [
                    {
                        label: 'Tenant setup',
                        complete: !!this.tenant,
                        detail: this.tenant ? `${this.tenant.name} (${this.tenant.tenantCode}) is available.` : 'Tenant details are missing.'
                    },
                    {
                        label: 'Identity provider',
                        complete: providerCount > 0,
                        detail: providerCount > 0 ? `${providerCount} provider configuration found.` : 'No identity provider configured.'
                    },
                    {
                        label: 'OIDC configuration',
                        complete: !!(result?.authContext?.identityProviders?.some((provider) => provider.oidcConfig?.issuerUri && provider.oidcConfig?.clientId) || this.tenant?.oidcConfig?.issuerUri),
                        detail: 'Issuer, client, and redirect settings should be validated before activation.'
                    },
                    {
                        label: 'Ingestion setup',
                        complete: !!result?.ingestionProfile && channelCount > 0,
                        detail: result?.ingestionProfile ? `${channelCount} ingestion channels configured.` : 'No ingestion profile configured.'
                    },
                    {
                        label: 'Partner onboarding',
                        complete: partners.length > 0,
                        detail: partners.length > 0 ? `${partners.length} codeshare partners configured.` : 'No partner agreements configured.'
                    }
                ];
            });
    }

    severity(complete: boolean) {
        return complete ? 'success' : 'warn';
    }
}
