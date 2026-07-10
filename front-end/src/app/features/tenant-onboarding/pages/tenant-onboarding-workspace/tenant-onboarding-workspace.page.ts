import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, NavigationEnd, Router, RouterModule } from '@angular/router';
import { TagModule } from 'primeng/tag';
import { filter, switchMap } from 'rxjs/operators';
import { forkJoin, of } from 'rxjs';
import { TenantService } from '@features/access-management/identity/tenants/services/tenant.service';
import { Tenant } from '@features/access-management/models/tenant.model';
import { TenantIngestionProfile } from '@features/access-management/identity/tenants/models/tenant-ingestion-profile.model';
import { TenantIngestionProfileService } from '@features/access-management/identity/tenants/services/tenant-ingestion-profile.service';
import { TenantPartnerService } from '@features/partner-management/tenant-partners/services/tenant-partners.service';
import { TenantPartnerProfileService } from '@features/partner-management/tenant-partners/services/tenant-partner-profile.service';
import { TenantPartnerCommunicationProfileService } from '@features/partner-management/tenant-partners/services/tenant-partner-communication-profile.service';
import { TenantPartnerDistributionProfileService } from '@features/partner-management/tenant-partners/services/tenant-partner-distribution-profile.service';
import {
    TENANT_ONBOARDING_STEPS,
    TenantOnboardingStep,
    TenantOnboardingStepKey,
    TenantOnboardingStepStatus
} from '../../tenant-onboarding-steps';

type StepMetrics = {
    tenant: boolean;
    identityProviders: number;
    hasOidcConfig: boolean;
    ingestionProfile: TenantIngestionProfile | null;
    partners: number;
    partnerProfiles: number;
    communicationProfiles: number;
    distributionProfiles: number;
};

@Component({
    selector: 'tenant-onboarding-workspace-page',
    standalone: true,
    imports: [CommonModule, RouterModule, TagModule],
    templateUrl: './tenant-onboarding-workspace.page.html',
    styleUrl: './tenant-onboarding-workspace.page.scss'
})
export class TenantOnboardingWorkspacePage {
    private readonly route = inject(ActivatedRoute);
    private readonly router = inject(Router);
    private readonly tenantService = inject(TenantService);
    private readonly ingestionService = inject(TenantIngestionProfileService);
    private readonly partnerService = inject(TenantPartnerService);
    private readonly partnerProfileService = inject(TenantPartnerProfileService);
    private readonly communicationProfileService = inject(TenantPartnerCommunicationProfileService);
    private readonly distributionProfileService = inject(TenantPartnerDistributionProfileService);

    tenant: Tenant | null = null;
    loading = true;
    currentStepKey: TenantOnboardingStepKey = 'tenant';
    completionCount = 0;
    steps = TENANT_ONBOARDING_STEPS;

    private metrics: StepMetrics = {
        tenant: false,
        identityProviders: 0,
        hasOidcConfig: false,
        ingestionProfile: null,
        partners: 0,
        partnerProfiles: 0,
        communicationProfiles: 0,
        distributionProfiles: 0
    };

    constructor() {
        this.route.paramMap
            .pipe(
                switchMap((params) => {
                    const id = params.get('id');
                    if (!id) {
                        return of(null);
                    }

                    this.loading = true;
                    return this.tenantService.getById(id);
                }),
                switchMap((tenant) => {
                    this.tenant = tenant;
                    if (!tenant?.tenantCode) {
                        return of(null);
                    }

                    const authContext$ = this.tenantService.getAuthContextByCode(tenant.tenantCode);
                    const ingestionProfile$ = this.ingestionService.getByTenantCode(tenant.tenantCode);
                    const partners$ = this.partnerService.getAll();
                    const partnerProfiles$ = this.partnerProfileService.getAll();
                    const communicationProfiles$ = this.communicationProfileService.getAll();
                    const distributionProfiles$ = this.distributionProfileService.getAll();

                    return forkJoin({
                        authContext: authContext$,
                        ingestionProfile: ingestionProfile$,
                        partners: partners$,
                        partnerProfiles: partnerProfiles$,
                        communicationProfiles: communicationProfiles$,
                        distributionProfiles: distributionProfiles$
                    });
                })
            )
            .subscribe((result) => {
                if (!this.tenant) {
                    this.loading = false;
                    return;
                }

                const tenantPartnerIds = (result?.partners ?? [])
                    .filter((partner) => partner.homeAirlineCode === this.tenant?.tenantCode)
                    .map((partner) => partner.id)
                    .filter((value): value is string => !!value);

                this.metrics = {
                    tenant: !!this.tenant,
                    identityProviders: result?.authContext?.identityProviders?.length ?? (this.tenant.authSource ? 1 : 0),
                    hasOidcConfig: !!(result?.authContext?.identityProviders?.some((provider) => provider.oidcConfig?.issuerUri && provider.oidcConfig?.clientId) || this.tenant.oidcConfig?.issuerUri),
                    ingestionProfile: result?.ingestionProfile ?? null,
                    partners: tenantPartnerIds.length,
                    partnerProfiles: (result?.partnerProfiles ?? []).filter((profile) => profile.partnerId && tenantPartnerIds.includes(profile.partnerId)).length,
                    communicationProfiles: (result?.communicationProfiles ?? []).filter((profile) => profile.partnerId && tenantPartnerIds.includes(profile.partnerId)).length,
                    distributionProfiles: (result?.distributionProfiles ?? []).filter((profile) => profile.partnerId && tenantPartnerIds.includes(profile.partnerId)).length
                };

                this.completionCount = this.steps.filter((step) => this.isStepComplete(step.key)).length;
                this.loading = false;
            });

        this.currentStepKey = (this.route.firstChild?.snapshot.routeConfig?.path as TenantOnboardingStepKey) || 'tenant';
        this.router.events.pipe(filter((event) => event instanceof NavigationEnd)).subscribe(() => {
            this.currentStepKey = (this.route.firstChild?.snapshot.routeConfig?.path as TenantOnboardingStepKey) || 'tenant';
        });
    }

    get stepProgressLabel() {
        return `${this.completionCount}/${this.steps.length}`;
    }

    get onboardingStatus() {
        return this.completionCount === this.steps.length ? 'Ready' : 'In Progress';
    }

    stepStatus(step: TenantOnboardingStep): TenantOnboardingStepStatus {
        if (step.key === this.currentStepKey) {
            return 'active';
        }
        return this.isStepComplete(step.key) ? 'complete' : 'pending';
    }

    isStepComplete(stepKey: TenantOnboardingStepKey): boolean {
        switch (stepKey) {
            case 'tenant':
                return this.metrics.tenant;
            case 'identity-providers':
                return this.metrics.identityProviders > 0;
            case 'oidc-config':
                return this.metrics.hasOidcConfig;
            case 'ingestion-profiles':
                return !!this.metrics.ingestionProfile;
            case 'ingestion-channels':
                return (this.metrics.ingestionProfile?.channels?.length ?? 0) > 0;
            case 'codeshare-partners':
                return this.metrics.partners > 0;
            case 'partner-profiles':
                return this.metrics.partnerProfiles > 0;
            case 'communication-profiles':
                return this.metrics.communicationProfiles > 0;
            case 'distribution-profiles':
                return this.metrics.distributionProfiles > 0;
            case 'review':
                return this.steps.slice(0, -1).every((step) => this.isStepComplete(step.key));
        }
    }

    stepRoute(step: TenantOnboardingStep) {
        return ['/tenant-onboarding', this.tenant?.id, step.route];
    }

    statusSeverity(status: TenantOnboardingStepStatus) {
        switch (status) {
            case 'complete':
                return 'success';
            case 'active':
                return 'info';
            default:
                return 'secondary';
        }
    }

    tenantStatusSeverity(status?: string | null) {
        switch (status) {
            case 'ACTIVE':
                return 'success';
            case 'SUSPENDED':
                return 'warn';
            case 'EXPIRED':
                return 'danger';
            default:
                return 'secondary';
        }
    }
}
