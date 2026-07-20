import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';

import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TagModule } from 'primeng/tag';
import { TextareaModule } from 'primeng/textarea';
import { TooltipModule } from 'primeng/tooltip';

import { AirlineCarrier } from '@features/masters/airlines/airline-carriers/models/airline-carriers.model';
import { AirlineCarrierService } from '@features/masters/airlines/airline-carriers/services/airline-carriers.service';
import { AuthSource, OidcConfig, Tenant } from '@features/administration/tenant-management/models/tenant.model';
import { TenantPartner } from '@features/administration/tenant-management/models/tenant-partner.model';
import { TenantPartnerCommunicationProfile, TenantPartnerDistributionProfile, TenantPartnerProfile } from '@features/administration/tenant-management/models/tenant-partner-profile.model';
import { TenantService } from '@features/administration/tenant-management/tenant-onboarding/tenant-administration/tenants/services/tenant.service';
import { TenantIngestionProfilesPage } from '@features/administration/tenant-management/tenant-onboarding/tenant-ingestion/pages/tenant-ingestion-profiles/tenant-ingestion-profiles.page';
import { TenantPartnerCommunicationProfileService } from '@features/administration/tenant-management/tenant-onboarding/tenant-partner-management/services/tenant-partner-communication-profile.service';
import { TenantPartnerDistributionProfileService } from '@features/administration/tenant-management/tenant-onboarding/tenant-partner-management/services/tenant-partner-distribution-profile.service';
import { TenantPartnerProfileService } from '@features/administration/tenant-management/tenant-onboarding/tenant-partner-management/services/tenant-partner-profile.service';
import { TenantPartnerService } from '@features/administration/tenant-management/tenant-onboarding/tenant-partner-management/services/tenant-partner.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { PermissionService } from '@core/security/permission.service';
import { AppConfirmService } from '@core/services/app-confirm.service';

type SetupSection = 'tenant' | 'identity' | 'partners' | 'ingestion';

@Component({
    selector: 'tenant-setup',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, ButtonModule, CheckboxModule, InputTextModule, SelectModule, TagModule, TextareaModule, TooltipModule, TenantIngestionProfilesPage],
    templateUrl: './tenant-setup.page.html',
    styleUrl: './tenant-setup.page.scss'
})
export class TenantSetupPage implements OnInit {
    private readonly fb = inject(FormBuilder);
    private readonly tenantService = inject(TenantService);
    private readonly partnerService = inject(TenantPartnerService);
    private readonly partnerProfileService = inject(TenantPartnerProfileService);
    private readonly communicationProfileService = inject(TenantPartnerCommunicationProfileService);
    private readonly distributionProfileService = inject(TenantPartnerDistributionProfileService);
    private readonly airlineService = inject(AirlineCarrierService);
    private readonly toast = inject(AppToastService);
    private readonly permissionService = inject(PermissionService);
    private readonly confirm = inject(AppConfirmService);

    activeSection: SetupSection = 'tenant';
    loading = false;
    savingTenant = false;
    savingPartner = false;
    editingPartnerId: number | null = null;
    selectedTenantId: number | null = null;
    selectedTenantIds = new Set<number>();
    tenantSearch = '';
    creatingTenant = false;

    tenant: Tenant | null = null;
    tenants: Tenant[] = [];
    partners: TenantPartner[] = [];
    partnerProfiles: TenantPartnerProfile[] = [];
    communicationProfiles: TenantPartnerCommunicationProfile[] = [];
    distributionProfiles: TenantPartnerDistributionProfile[] = [];
    airlines: AirlineCarrier[] = [];

    readonly sections: Array<{ key: SetupSection; label: string; icon: string }> = [
        { key: 'tenant', label: 'Tenant', icon: 'pi pi-building' },
        { key: 'identity', label: 'Identity', icon: 'pi pi-key' },
        { key: 'partners', label: 'Partners', icon: 'pi pi-share-alt' },
        { key: 'ingestion', label: 'Ingestion', icon: 'pi pi-upload' }
    ];

    readonly statusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Suspended', value: 'SUSPENDED' },
        { label: 'Expired', value: 'EXPIRED' },
        { label: 'Deleted', value: 'DELETED' }
    ];

    readonly planOptions = [
        { label: 'Free', value: 'FREE' },
        { label: 'Pro', value: 'PRO' },
        { label: 'Enterprise', value: 'ENTERPRISE' }
    ];

    readonly authSourceOptions = [
        { label: 'Internal users', value: AuthSource.INTERNAL },
        { label: 'LDAP', value: AuthSource.LDAP },
        { label: 'Azure AD', value: AuthSource.AZURE },
        { label: 'Keycloak', value: AuthSource.KEYCLOAK },
        { label: 'Okta', value: AuthSource.OKTA },
        { label: 'Generic OIDC', value: AuthSource.OIDC_GENERIC }
    ];

    readonly agreementTypeOptions = [
        { label: 'Free Sale', value: 'FREE_SALE' },
        { label: 'Block Space', value: 'BLOCK_SPACE' },
        { label: 'Segmented', value: 'SEGMENTED' },
        { label: 'Wet Lease', value: 'WET_LEASE' }
    ];

    readonly agreementStatusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Draft', value: 'DRAFT' },
        { label: 'Suspended', value: 'SUSPENDED' },
        { label: 'Expired', value: 'EXPIRED' }
    ];

    tenantForm = this.fb.group({
        name: ['', Validators.required],
        tenantCode: [{ value: '', disabled: true }, Validators.required],
        status: ['ACTIVE'],
        plan: ['ENTERPRISE'],
        subscriptionStart: [''],
        subscriptionEnd: [''],
        region: [''],
        contactEmail: ['', Validators.email],
        contactPhone: [''],
        logoUrl: [''],
        trial: [false],
        description: ['']
    });

    identityForm = this.fb.group({
        authSource: [AuthSource.INTERNAL, Validators.required],
        issuerUri: [''],
        authorizationUri: [''],
        tokenUri: [''],
        jwkSetUri: [''],
        clientId: [''],
        clientSecretRef: [''],
        redirectUri: [''],
        scopes: ['openid profile email'],
        enforceRedirectUri: [true]
    });

    partnerForm = this.fb.group({
        homeAirlineId: [null as number | null, Validators.required],
        partnerAirlineId: [null as number | null, Validators.required],
        agreementNumber: [''],
        agreementType: ['FREE_SALE', Validators.required],
        agreementStatus: ['ACTIVE', Validators.required],
        effectiveFrom: [''],
        effectiveTo: [''],
        active: [true],
        displayOrder: [1],
        description: [''],
        remarks: ['']
    });

    get airlineOptions() {
        return this.airlines.map((airline) => ({
            label: `${airline.iataCode || airline.icaoCode || airline.id} - ${airline.displayName || airline.commercialName || airline.legalName || 'Airline'}`,
            value: Number(airline.id)
        }));
    }

    get tenantAdminMode(): boolean {
        return this.permissionService.hasAnyRole(['TENANT_ADMIN', 'SYSTEM_ADMIN']) || this.permissionService.hasRawPermission('*');
    }

    get oidcRequired(): boolean {
        return this.identityForm.controls.authSource.value !== AuthSource.INTERNAL;
    }

    get selectedTenantLabel(): string {
        if (this.creatingTenant) {
            return 'New tenant';
        }

        return this.tenant?.name || 'Tenant Setup';
    }

    get filteredTenants(): Tenant[] {
        const search = this.tenantSearch.trim().toLowerCase();
        if (!search) {
            return this.tenants;
        }

        return this.tenants.filter((tenant) =>
            [tenant.name, tenant.tenantCode, tenant.status, tenant.plan, tenant.region, tenant.contactEmail]
                .filter((value): value is string => typeof value === 'string')
                .some((value) => value.toLowerCase().includes(search))
        );
    }

    get selectedTenantsCount(): number {
        return this.selectedTenantIds.size;
    }

    get completedCount(): number {
        return [
            this.tenant?.name,
            this.identityForm.controls.authSource.value,
            this.partners.length > 0,
            this.partnerProfiles.length > 0 || this.communicationProfiles.length > 0 || this.distributionProfiles.length > 0
        ].filter(Boolean).length;
    }

    get partnerSetupCount(): number {
        return this.partnerProfiles.length + this.communicationProfiles.length + this.distributionProfiles.length;
    }

    ngOnInit(): void {
        this.load();
    }

    load(): void {
        this.loading = true;

        if (this.tenantAdminMode) {
            forkJoin({
                tenants: this.tenantService.getAll(),
                airlines: this.airlineService.getAll({ active: 'true' })
            }).subscribe({
                next: ({ tenants, airlines }) => {
                    this.tenants = tenants;
                    this.selectedTenantIds = new Set(Array.from(this.selectedTenantIds).filter((id) => tenants.some((tenant) => tenant.id === id)));
                    this.airlines = airlines;
                    const selected = this.resolveSelectedTenant(tenants);
                    if (!selected?.id) {
                        this.loading = false;
                        this.clearTenantWorkspace();
                        return;
                    }
                    this.selectTenant(selected);
                },
                error: () => {
                    this.loading = false;
                    this.toast.error('Failed to load tenant setup');
                }
            });
            return;
        }

        this.loadCurrentTenant();
    }

    selectTenant(tenant: Tenant): void {
        if (!tenant.id) {
            return;
        }

        this.creatingTenant = false;
        this.loading = true;
        this.selectedTenantId = tenant.id;
        forkJoin({
            tenant: this.tenantService.getById(tenant.id),
            partners: this.partnerService.getAll({ tenantId: String(tenant.id) }),
            partnerProfiles: this.partnerProfileService.getAll(),
            communicationProfiles: this.communicationProfileService.getAll(),
            distributionProfiles: this.distributionProfileService.getAll()
        }).subscribe({
            next: ({ tenant, partners, partnerProfiles, communicationProfiles, distributionProfiles }) => {
                this.tenant = tenant;
                this.partners = partners;
                const partnerIds = new Set(partners.map((partner) => partner.id).filter((id): id is number => id !== undefined));
                this.partnerProfiles = partnerProfiles.filter((profile) => profile.partnerId !== undefined && partnerIds.has(profile.partnerId));
                this.communicationProfiles = communicationProfiles.filter((profile) => profile.partnerId !== undefined && partnerIds.has(profile.partnerId));
                this.distributionProfiles = distributionProfiles.filter((profile) => profile.partnerId !== undefined && partnerIds.has(profile.partnerId));
                this.patchTenant(tenant);
                this.resetPartnerForm();
                this.loading = false;
            },
            error: () => {
                this.loading = false;
                this.toast.error('Failed to load selected tenant');
            }
        });
    }

    show(section: SetupSection): void {
        this.activeSection = section;
    }

    saveTenant(): void {
        if (this.tenantForm.invalid || this.identityForm.invalid || this.savingTenant) {
            this.tenantForm.markAllAsTouched();
            this.identityForm.markAllAsTouched();
            return;
        }

        this.savingTenant = true;
        const payload = this.toTenantPayload();
        const request = this.tenantAdminMode && this.creatingTenant
            ? this.tenantService.create(payload as Tenant)
            : this.tenantAdminMode && this.selectedTenantId
                ? this.tenantService.update(this.selectedTenantId, payload as Tenant)
                : this.tenantService.updateCurrent(payload);

        request.subscribe({
            next: (tenant) => {
                this.tenant = tenant;
                this.selectedTenantId = tenant.id ?? null;
                this.creatingTenant = false;
                this.patchTenant(tenant);
                this.replaceTenantInDirectory(tenant);
                this.savingTenant = false;
                if (this.tenantAdminMode) {
                    this.load();
                }
            },
            error: () => {
                this.savingTenant = false;
            }
        });
    }

    startCreateTenant(): void {
        if (!this.tenantAdminMode) {
            return;
        }

        this.creatingTenant = true;
        this.selectedTenantId = null;
        this.tenant = null;
        this.partners = [];
        this.partnerProfiles = [];
        this.communicationProfiles = [];
        this.distributionProfiles = [];
        this.editingPartnerId = null;
        this.tenantForm.controls.tenantCode.enable();
        this.tenantForm.reset({
            name: '',
            tenantCode: '',
            status: 'ACTIVE',
            plan: 'ENTERPRISE',
            subscriptionStart: '',
            subscriptionEnd: '',
            region: '',
            contactEmail: '',
            contactPhone: '',
            logoUrl: '',
            trial: false,
            description: ''
        });
        this.identityForm.reset({
            authSource: AuthSource.INTERNAL,
            issuerUri: '',
            authorizationUri: '',
            tokenUri: '',
            jwkSetUri: '',
            clientId: '',
            clientSecretRef: '',
            redirectUri: '',
            scopes: 'openid profile email',
            enforceRedirectUri: true
        });
        this.resetPartnerForm();
        this.activeSection = 'tenant';
    }

    deleteTenant(tenant: Tenant): void {
        if (!this.tenantAdminMode || !tenant.id || this.savingTenant) {
            return;
        }

        this.confirm.delete(`Delete tenant "${tenant.name || tenant.tenantCode}"?`, () => {
            this.savingTenant = true;
            this.tenantService.delete(tenant.id!).subscribe({
                next: () => {
                    this.savingTenant = false;
                    this.creatingTenant = false;
                    if (this.selectedTenantId === tenant.id) {
                        this.selectedTenantId = null;
                    }
                    this.load();
                },
                error: () => {
                    this.savingTenant = false;
                }
            });
        });
    }

    deleteSelectedTenant(): void {
        const tenant = this.tenants.find((item) => item.id === this.selectedTenantId) ?? this.tenant;
        if (tenant) {
            this.deleteTenant(tenant);
        }
    }

    toggleTenantSelection(tenant: Tenant, selected: boolean): void {
        if (!tenant.id) {
            return;
        }

        const next = new Set(this.selectedTenantIds);
        if (selected) {
            next.add(tenant.id);
        } else {
            next.delete(tenant.id);
        }
        this.selectedTenantIds = next;
    }

    deleteSelectedTenants(): void {
        const ids = Array.from(this.selectedTenantIds);
        if (!this.tenantAdminMode || !ids.length || this.savingTenant) {
            return;
        }

        this.confirm.delete(`Delete ${ids.length} selected tenant(s)?`, () => {
            this.savingTenant = true;
            forkJoin(ids.map((id) => this.tenantService.delete(id))).subscribe({
                next: () => {
                    this.savingTenant = false;
                    this.selectedTenantIds = new Set();
                    if (this.selectedTenantId && ids.includes(this.selectedTenantId)) {
                        this.selectedTenantId = null;
                    }
                    this.load();
                },
                error: () => {
                    this.savingTenant = false;
                }
            });
        });
    }

    exportTenantsCsv(): void {
        const headers = ['Tenant Code', 'Name', 'Status', 'Plan', 'Region', 'Contact Email'];
        const rows = this.filteredTenants.map((tenant) => [
            tenant.tenantCode,
            tenant.name,
            tenant.status,
            tenant.plan,
            tenant.region,
            tenant.contactEmail
        ]);
        const csv = [headers, ...rows].map((row) => row.map((value) => this.csvCell(value)).join(',')).join('\n');
        const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = 'tenants.csv';
        link.click();
        URL.revokeObjectURL(link.href);
    }

    editPartner(partner: TenantPartner): void {
        this.editingPartnerId = partner.id ?? null;
        this.partnerForm.patchValue({
            homeAirlineId: partner.homeAirlineId ?? null,
            partnerAirlineId: partner.partnerAirlineId ?? null,
            agreementNumber: partner.agreementNumber ?? '',
            agreementType: partner.agreementType ?? 'FREE_SALE',
            agreementStatus: partner.agreementStatus ?? 'ACTIVE',
            effectiveFrom: this.toDateInput(partner.effectiveFrom),
            effectiveTo: this.toDateInput(partner.effectiveTo),
            active: partner.active ?? true,
            displayOrder: partner.displayOrder ?? 1,
            description: partner.description ?? '',
            remarks: partner.remarks ?? ''
        });
        this.activeSection = 'partners';
    }

    resetPartnerForm(): void {
        this.editingPartnerId = null;
        this.partnerForm.reset({
            homeAirlineId: null,
            partnerAirlineId: null,
            agreementNumber: '',
            agreementType: 'FREE_SALE',
            agreementStatus: 'ACTIVE',
            effectiveFrom: '',
            effectiveTo: '',
            active: true,
            displayOrder: this.partners.length + 1,
            description: '',
            remarks: ''
        });
    }

    savePartner(): void {
        if (this.partnerForm.invalid || this.savingPartner) {
            this.partnerForm.markAllAsTouched();
            return;
        }

        this.savingPartner = true;
        const payload = this.toPartnerPayload();
        if (this.tenantAdminMode && this.selectedTenantId) {
            payload.tenantId = this.selectedTenantId;
        }

        const request = this.editingPartnerId
            ? this.tenantAdminMode
                ? this.partnerService.update(this.editingPartnerId, payload)
                : this.partnerService.updateCurrent(this.editingPartnerId, payload)
            : this.tenantAdminMode
                ? this.partnerService.create(payload)
                : this.partnerService.createCurrent(payload);

        request.subscribe({
            next: () => {
                this.savingPartner = false;
                this.resetPartnerForm();
                this.reloadPartners();
            },
            error: () => {
                this.savingPartner = false;
            }
        });
    }

    removePartner(partner: TenantPartner): void {
        if (!partner.id) {
            return;
        }

        const request = this.tenantAdminMode ? this.partnerService.delete(partner.id) : this.partnerService.deleteCurrent(partner.id);
        request.subscribe({
            next: () => this.reloadPartners()
        });
    }

    partnerLabel(partner: TenantPartner): string {
        return `${partner.partnerAirlineCode || partner.partnerAirlineId || '-'} ${partner.partnerAirlineName || ''}`.trim();
    }

    businessProfileCount(partner: TenantPartner): number {
        return this.countByPartner(this.partnerProfiles, partner);
    }

    communicationProfileCount(partner: TenantPartner): number {
        return this.countByPartner(this.communicationProfiles, partner);
    }

    distributionProfileCount(partner: TenantPartner): number {
        return this.countByPartner(this.distributionProfiles, partner);
    }

    private reloadPartners(): void {
        if (this.tenantAdminMode && this.selectedTenantId) {
            const selected = this.tenants.find((tenant) => tenant.id === this.selectedTenantId) ?? this.tenant;
            if (selected) {
                this.selectTenant(selected);
            }
            return;
        }

        forkJoin({
            partners: this.partnerService.getCurrent(),
            partnerProfiles: this.partnerProfileService.getCurrent(),
            communicationProfiles: this.communicationProfileService.getCurrent(),
            distributionProfiles: this.distributionProfileService.getCurrent()
        }).subscribe({
            next: ({ partners, partnerProfiles, communicationProfiles, distributionProfiles }) => {
                this.partners = partners;
                this.partnerProfiles = partnerProfiles;
                this.communicationProfiles = communicationProfiles;
                this.distributionProfiles = distributionProfiles;
            },
            error: () => this.toast.error('Failed to reload codeshare partners')
        });
    }

    private loadCurrentTenant(): void {
        forkJoin({
            tenant: this.tenantService.getCurrent(),
            partners: this.partnerService.getCurrent(),
            partnerProfiles: this.partnerProfileService.getCurrent(),
            communicationProfiles: this.communicationProfileService.getCurrent(),
            distributionProfiles: this.distributionProfileService.getCurrent(),
            airlines: this.airlineService.getAll({ active: 'true' })
        }).subscribe({
            next: ({ tenant, partners, partnerProfiles, communicationProfiles, distributionProfiles, airlines }) => {
                this.tenant = tenant;
                this.selectedTenantId = tenant.id ?? null;
                this.partners = partners;
                this.partnerProfiles = partnerProfiles;
                this.communicationProfiles = communicationProfiles;
                this.distributionProfiles = distributionProfiles;
                this.airlines = airlines;
                this.patchTenant(tenant);
                this.loading = false;
            },
            error: () => {
                this.loading = false;
                this.toast.error('Failed to load tenant setup');
            }
        });
    }

    private resolveSelectedTenant(tenants: Tenant[]): Tenant | undefined {
        return tenants.find((tenant) => tenant.id === this.selectedTenantId) ?? tenants[0];
    }

    private clearTenantWorkspace(): void {
        this.tenant = null;
        this.selectedTenantId = null;
        this.partners = [];
        this.partnerProfiles = [];
        this.communicationProfiles = [];
        this.distributionProfiles = [];
        this.tenantForm.reset();
        this.identityForm.reset({
            authSource: AuthSource.INTERNAL,
            scopes: 'openid profile email',
            enforceRedirectUri: true
        });
        this.resetPartnerForm();
    }

    private replaceTenantInDirectory(tenant: Tenant): void {
        if (!tenant.id) {
            return;
        }

        const exists = this.tenants.some((item) => item.id === tenant.id);
        this.tenants = exists
            ? this.tenants.map((item) => item.id === tenant.id ? tenant : item)
            : [tenant, ...this.tenants];
    }

    private csvCell(value: unknown): string {
        const text = value === undefined || value === null ? '' : String(value);
        return `"${text.replace(/"/g, '""')}"`;
    }

    private countByPartner<T extends { partnerId?: number }>(items: T[], partner: TenantPartner): number {
        return partner.id ? items.filter((item) => item.partnerId === partner.id).length : 0;
    }

    private patchTenant(tenant: Tenant): void {
        this.tenantForm.controls.tenantCode.disable();
        const provider = tenant.identityProviders?.[0];
        const oidc = provider?.oidcConfig ?? tenant.oidcConfig ?? {};

        this.tenantForm.patchValue({
            name: tenant.name ?? '',
            tenantCode: tenant.tenantCode ?? '',
            status: tenant.status ?? 'ACTIVE',
            plan: tenant.plan ?? 'ENTERPRISE',
            subscriptionStart: this.toDateTimeLocal(tenant.subscriptionStart),
            subscriptionEnd: this.toDateTimeLocal(tenant.subscriptionEnd),
            region: tenant.region ?? '',
            contactEmail: tenant.contactEmail ?? '',
            contactPhone: tenant.contactPhone ?? '',
            logoUrl: tenant.logoUrl ?? '',
            trial: tenant.trial ?? false,
            description: tenant.description ?? ''
        });

        this.identityForm.patchValue({
            authSource: provider?.authSource ?? tenant.authSource ?? AuthSource.INTERNAL,
            issuerUri: oidc.issuerUri ?? '',
            authorizationUri: oidc.authorizationUri ?? '',
            tokenUri: oidc.tokenUri ?? '',
            jwkSetUri: oidc.jwkSetUri ?? '',
            clientId: oidc.clientId ?? '',
            clientSecretRef: oidc.clientSecretRef ?? '',
            redirectUri: oidc.redirectUri ?? '',
            scopes: oidc.scopes ?? 'openid profile email',
            enforceRedirectUri: oidc.enforceRedirectUri ?? true
        });
    }

    private toTenantPayload(): Partial<Tenant> {
        const tenant = this.tenantForm.getRawValue();
        const identity = this.identityForm.getRawValue();
        const oidcConfig: OidcConfig = {
            issuerUri: identity.issuerUri?.trim() || undefined,
            authorizationUri: identity.authorizationUri?.trim() || undefined,
            tokenUri: identity.tokenUri?.trim() || undefined,
            jwkSetUri: identity.jwkSetUri?.trim() || undefined,
            clientId: identity.clientId?.trim() || undefined,
            clientSecretRef: identity.clientSecretRef?.trim() || undefined,
            redirectUri: identity.redirectUri?.trim() || undefined,
            scopes: identity.scopes?.trim() || undefined,
            enforceRedirectUri: !!identity.enforceRedirectUri
        };

        return {
            name: tenant.name?.trim() || '',
            tenantCode: tenant.tenantCode?.trim() || undefined,
            status: tenant.status as Tenant['status'],
            plan: tenant.plan as Tenant['plan'],
            subscriptionStart: this.parseDateTimeLocal(tenant.subscriptionStart),
            subscriptionEnd: this.parseDateTimeLocal(tenant.subscriptionEnd),
            region: tenant.region?.trim() || undefined,
            contactEmail: tenant.contactEmail?.trim() || undefined,
            contactPhone: tenant.contactPhone?.trim() || undefined,
            logoUrl: tenant.logoUrl?.trim() || undefined,
            trial: !!tenant.trial,
            description: tenant.description?.trim() || undefined,
            authSource: identity.authSource ?? AuthSource.INTERNAL,
            oidcConfig
        };
    }

    private toPartnerPayload(): TenantPartner {
        const raw = this.partnerForm.getRawValue();
        return {
            homeAirlineId: raw.homeAirlineId ?? undefined,
            partnerAirlineId: raw.partnerAirlineId ?? undefined,
            agreementNumber: raw.agreementNumber?.trim() || undefined,
            agreementType: raw.agreementType ?? undefined,
            agreementStatus: raw.agreementStatus ?? undefined,
            effectiveFrom: raw.effectiveFrom || undefined,
            effectiveTo: raw.effectiveTo || undefined,
            active: !!raw.active,
            displayOrder: this.toNumber(raw.displayOrder) ?? 1,
            description: raw.description?.trim() || undefined,
            remarks: raw.remarks?.trim() || undefined,
            recordStatus: 'ACTIVE'
        };
    }

    private toDateInput(value?: string): string {
        return value ? value.slice(0, 10) : '';
    }

    private toDateTimeLocal(value?: string | null): string {
        if (!value) {
            return '';
        }
        const date = new Date(value);
        if (Number.isNaN(date.getTime())) {
            return '';
        }
        const pad = (n: number) => String(n).padStart(2, '0');
        return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
    }

    private parseDateTimeLocal(value?: string | null): string | undefined {
        if (!value) {
            return undefined;
        }
        const date = new Date(value);
        return Number.isNaN(date.getTime()) ? undefined : date.toISOString();
    }

    private toNumber(value: unknown): number | undefined {
        if (value === null || value === undefined || value === '') {
            return undefined;
        }
        const parsed = Number(value);
        return Number.isFinite(parsed) ? parsed : undefined;
    }
}
