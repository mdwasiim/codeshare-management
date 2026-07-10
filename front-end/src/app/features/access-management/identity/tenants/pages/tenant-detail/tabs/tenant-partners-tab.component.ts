import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { CheckboxModule } from 'primeng/checkbox';
import { AppDialogComponent } from '@shared/components/app-dialog/app-dialog.component';
import { AppFormSectionComponent } from '@shared/components/form-section/app-form-section.component';
import { TenantService } from '../../../services/tenant.service';
import { Tenant } from '@features/access-management/models/tenant.model';
import { TenantPartner } from '@features/partner-management/tenant-partners/models/tenant-partner.model';
import { TenantPartnerProfile } from '@features/partner-management/tenant-partners/models/tenant-partner-profile.model';
import { TenantPartnerCommunicationProfile } from '@features/partner-management/tenant-partners/models/tenant-partner-communication-profile.model';
import { TenantPartnerDistributionProfile } from '@features/partner-management/tenant-partners/models/tenant-partner-distribution-profile.model';
import { TenantPartnerService } from '@features/partner-management/tenant-partners/services/tenant-partners.service';
import { TenantPartnerProfileService } from '@features/partner-management/tenant-partners/services/tenant-partner-profile.service';
import { TenantPartnerCommunicationProfileService } from '@features/partner-management/tenant-partners/services/tenant-partner-communication-profile.service';
import { TenantPartnerDistributionProfileService } from '@features/partner-management/tenant-partners/services/tenant-partner-distribution-profile.service';
import { AppToastService } from '@services/toast/app-toast.service';

@Component({
    selector: 'tenant-partners-tab',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        TableModule,
        ButtonModule,
        TagModule,
        InputTextModule,
        SelectModule,
        CheckboxModule,
        AppDialogComponent,
        AppFormSectionComponent
    ],
    templateUrl: './tenant-partners-tab.component.html'
})
export class TenantPartnersTabComponent {
    private readonly route = inject(ActivatedRoute);
    private readonly tenantService = inject(TenantService);
    private readonly partnerService = inject(TenantPartnerService);
    private readonly partnerProfileService = inject(TenantPartnerProfileService);
    private readonly communicationProfileService = inject(TenantPartnerCommunicationProfileService);
    private readonly distributionProfileService = inject(TenantPartnerDistributionProfileService);
    private readonly fb = inject(FormBuilder);
    private readonly toast = inject(AppToastService);

    tenant: Tenant | null = null;
    partners: TenantPartner[] = [];
    selectedPartner: TenantPartner | null = null;
    partnerProfiles: TenantPartnerProfile[] = [];
    communicationProfiles: TenantPartnerCommunicationProfile[] = [];
    distributionProfiles: TenantPartnerDistributionProfile[] = [];

    partnerDialogVisible = false;
    profileDialogVisible = false;
    communicationDialogVisible = false;
    distributionDialogVisible = false;

    readonly partnerForm = this.fb.group({
        id: [''],
        tenantId: [''],
        homeAirlineId: [''],
        homeAirlineCode: [''],
        homeAirlineName: [''],
        partnerAirlineId: [''],
        partnerAirlineCode: ['', Validators.required],
        partnerAirlineName: ['', Validators.required],
        agreementNumber: [''],
        agreementType: ['BILATERAL', Validators.required],
        agreementStatus: ['ACTIVE', Validators.required],
        active: [true],
        displayOrder: [1],
        description: [''],
        remarks: [''],
        recordStatus: ['ACTIVE'],
        effectiveFrom: [''],
        effectiveTo: ['']
    });

    readonly partnerProfileForm = this.fb.group({
        id: [''],
        partnerId: ['', Validators.required],
        profileCode: ['', Validators.required],
        profileName: ['', Validators.required],
        partnerType: ['ONLINE'],
        agreementCategory: ['STANDARD'],
        inventorySharingType: ['ALLOTMENT'],
        priority: [1],
        autoAcceptScheduleChanges: [false],
        prorationApplicable: [false],
        eTicketAllowed: [false],
        active: [true],
        displayOrder: [1],
        description: [''],
        remarks: [''],
        recordStatus: ['ACTIVE'],
        effectiveFrom: [''],
        effectiveTo: ['']
    });

    readonly communicationProfileForm = this.fb.group({
        id: [''],
        partnerId: ['', Validators.required],
        profileCode: ['', Validators.required],
        profileName: ['', Validators.required],
        protocol: ['API'],
        transportType: ['HTTPS'],
        messageFormat: ['JSON'],
        authenticationType: ['BASIC'],
        endpointUrl: [''],
        username: [''],
        credentialAlias: [''],
        connectionTimeout: [30000],
        readTimeout: [30000],
        retryCount: [3],
        compressionEnabled: [false],
        encryptionEnabled: [false],
        active: [true],
        displayOrder: [1],
        description: [''],
        remarks: [''],
        recordStatus: ['ACTIVE'],
        effectiveFrom: [''],
        effectiveTo: ['']
    });

    readonly distributionProfileForm = this.fb.group({
        id: [''],
        partnerId: ['', Validators.required],
        profileCode: ['', Validators.required],
        profileName: ['', Validators.required],
        distributionChannel: ['API'],
        distributionMode: ['PUSH'],
        messageType: ['SSIM'],
        realTimeEnabled: [false],
        acknowledgementRequired: [false],
        retryEnabled: [false],
        retryCount: [0],
        active: [true],
        displayOrder: [1],
        description: [''],
        remarks: [''],
        recordStatus: ['ACTIVE'],
        effectiveFrom: [''],
        effectiveTo: ['']
    });

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
                this.loadPartners();
            });
    }

    loadPartners() {
        this.partnerService.getAll().subscribe((partners) => {
            this.partners = partners.filter((partner) => partner.homeAirlineCode === this.tenant?.tenantCode);
            if (this.selectedPartner?.id) {
                const selected = this.partners.find((partner) => partner.id === this.selectedPartner?.id) || null;
                this.selectPartner(selected);
            } else if (this.partners.length) {
                this.selectPartner(this.partners[0]);
            } else {
                this.selectPartner(null);
            }
        });
    }

    selectPartner(partner: TenantPartner | null) {
        this.selectedPartner = partner;
        if (!partner?.id) {
            this.partnerProfiles = [];
            this.communicationProfiles = [];
            this.distributionProfiles = [];
            return;
        }

        this.partnerProfileService.getAll().subscribe((profiles) => {
            this.partnerProfiles = profiles.filter((profile) => profile.partnerId === partner.id);
        });
        this.communicationProfileService.getAll().subscribe((profiles) => {
            this.communicationProfiles = profiles.filter((profile) => profile.partnerId === partner.id);
        });
        this.distributionProfileService.getAll().subscribe((profiles) => {
            this.distributionProfiles = profiles.filter((profile) => profile.partnerId === partner.id);
        });
    }

    openPartnerDialog(partner?: TenantPartner) {
        this.partnerForm.reset({
            id: partner?.id || '',
            tenantId: partner?.tenantId || this.tenant?.id || '',
            homeAirlineId: partner?.homeAirlineId || '',
            homeAirlineCode: partner?.homeAirlineCode || this.tenant?.tenantCode || '',
            homeAirlineName: partner?.homeAirlineName || this.tenant?.name || '',
            partnerAirlineId: partner?.partnerAirlineId || '',
            partnerAirlineCode: partner?.partnerAirlineCode || '',
            partnerAirlineName: partner?.partnerAirlineName || '',
            agreementNumber: partner?.agreementNumber || '',
            agreementType: partner?.agreementType || 'BILATERAL',
            agreementStatus: partner?.agreementStatus || 'ACTIVE',
            active: partner?.active ?? true,
            displayOrder: partner?.displayOrder ?? this.partners.length + 1,
            description: partner?.description || '',
            remarks: partner?.remarks || '',
            recordStatus: partner?.recordStatus || 'ACTIVE',
            effectiveFrom: partner?.effectiveFrom || '',
            effectiveTo: partner?.effectiveTo || ''
        });
        this.partnerDialogVisible = true;
    }

    savePartner() {
        if (this.partnerForm.invalid) {
            this.partnerForm.markAllAsTouched();
            return;
        }
        const payload = this.partnerForm.getRawValue() as TenantPartner;
        const request = payload.id ? this.partnerService.update(payload.id, payload) : this.partnerService.create(payload);
        request.subscribe(() => {
            this.partnerDialogVisible = false;
            this.loadPartners();
        });
    }

    deletePartner(partner: TenantPartner) {
        if (!partner.id) return;
        this.partnerService.delete(partner.id).subscribe(() => this.loadPartners());
    }

    openPartnerProfileDialog(profile?: TenantPartnerProfile) {
        if (!this.selectedPartner?.id) {
            this.toast.warn('Select a partner first');
            return;
        }
        this.partnerProfileForm.reset({
            id: profile?.id || '',
            partnerId: profile?.partnerId || this.selectedPartner.id,
            profileCode: profile?.profileCode || '',
            profileName: profile?.profileName || '',
            partnerType: profile?.partnerType || 'ONLINE',
            agreementCategory: profile?.agreementCategory || 'STANDARD',
            inventorySharingType: profile?.inventorySharingType || 'ALLOTMENT',
            priority: profile?.priority ?? this.partnerProfiles.length + 1,
            autoAcceptScheduleChanges: profile?.autoAcceptScheduleChanges ?? false,
            prorationApplicable: profile?.prorationApplicable ?? false,
            eTicketAllowed: profile?.eTicketAllowed ?? false,
            active: profile?.active ?? true,
            displayOrder: profile?.displayOrder ?? this.partnerProfiles.length + 1,
            description: profile?.description || '',
            remarks: profile?.remarks || '',
            recordStatus: profile?.recordStatus || 'ACTIVE',
            effectiveFrom: profile?.effectiveFrom || '',
            effectiveTo: profile?.effectiveTo || ''
        });
        this.profileDialogVisible = true;
    }

    savePartnerProfile() {
        if (this.partnerProfileForm.invalid) {
            this.partnerProfileForm.markAllAsTouched();
            return;
        }
        const payload = this.partnerProfileForm.getRawValue() as TenantPartnerProfile;
        const request = payload.id ? this.partnerProfileService.update(payload.id, payload) : this.partnerProfileService.create(payload);
        request.subscribe(() => {
            this.profileDialogVisible = false;
            this.selectPartner(this.selectedPartner);
        });
    }

    deletePartnerProfile(profile: TenantPartnerProfile) {
        if (!profile.id) return;
        this.partnerProfileService.delete(profile.id).subscribe(() => this.selectPartner(this.selectedPartner));
    }

    openCommunicationDialog(profile?: TenantPartnerCommunicationProfile) {
        if (!this.selectedPartner?.id) {
            this.toast.warn('Select a partner first');
            return;
        }
        this.communicationProfileForm.reset({
            id: profile?.id || '',
            partnerId: profile?.partnerId || this.selectedPartner.id,
            profileCode: profile?.profileCode || '',
            profileName: profile?.profileName || '',
            protocol: profile?.protocol || 'API',
            transportType: profile?.transportType || 'HTTPS',
            messageFormat: profile?.messageFormat || 'JSON',
            authenticationType: profile?.authenticationType || 'BASIC',
            endpointUrl: profile?.endpointUrl || '',
            username: profile?.username || '',
            credentialAlias: profile?.credentialAlias || '',
            connectionTimeout: profile?.connectionTimeout ?? 30000,
            readTimeout: profile?.readTimeout ?? 30000,
            retryCount: profile?.retryCount ?? 3,
            compressionEnabled: profile?.compressionEnabled ?? false,
            encryptionEnabled: profile?.encryptionEnabled ?? false,
            active: profile?.active ?? true,
            displayOrder: profile?.displayOrder ?? this.communicationProfiles.length + 1,
            description: profile?.description || '',
            remarks: profile?.remarks || '',
            recordStatus: profile?.recordStatus || 'ACTIVE',
            effectiveFrom: profile?.effectiveFrom || '',
            effectiveTo: profile?.effectiveTo || ''
        });
        this.communicationDialogVisible = true;
    }

    saveCommunicationProfile() {
        if (this.communicationProfileForm.invalid) {
            this.communicationProfileForm.markAllAsTouched();
            return;
        }
        const payload = this.communicationProfileForm.getRawValue() as TenantPartnerCommunicationProfile;
        const request = payload.id ? this.communicationProfileService.update(payload.id, payload) : this.communicationProfileService.create(payload);
        request.subscribe(() => {
            this.communicationDialogVisible = false;
            this.selectPartner(this.selectedPartner);
        });
    }

    deleteCommunicationProfile(profile: TenantPartnerCommunicationProfile) {
        if (!profile.id) return;
        this.communicationProfileService.delete(profile.id).subscribe(() => this.selectPartner(this.selectedPartner));
    }

    openDistributionDialog(profile?: TenantPartnerDistributionProfile) {
        if (!this.selectedPartner?.id) {
            this.toast.warn('Select a partner first');
            return;
        }
        this.distributionProfileForm.reset({
            id: profile?.id || '',
            partnerId: profile?.partnerId || this.selectedPartner.id,
            profileCode: profile?.profileCode || '',
            profileName: profile?.profileName || '',
            distributionChannel: profile?.distributionChannel || 'API',
            distributionMode: profile?.distributionMode || 'PUSH',
            messageType: profile?.messageType || 'SSIM',
            realTimeEnabled: profile?.realTimeEnabled ?? false,
            acknowledgementRequired: profile?.acknowledgementRequired ?? false,
            retryEnabled: profile?.retryEnabled ?? false,
            retryCount: profile?.retryCount ?? 0,
            active: profile?.active ?? true,
            displayOrder: profile?.displayOrder ?? this.distributionProfiles.length + 1,
            description: profile?.description || '',
            remarks: profile?.remarks || '',
            recordStatus: profile?.recordStatus || 'ACTIVE',
            effectiveFrom: profile?.effectiveFrom || '',
            effectiveTo: profile?.effectiveTo || ''
        });
        this.distributionDialogVisible = true;
    }

    saveDistributionProfile() {
        if (this.distributionProfileForm.invalid) {
            this.distributionProfileForm.markAllAsTouched();
            return;
        }
        const payload = this.distributionProfileForm.getRawValue() as TenantPartnerDistributionProfile;
        const request = payload.id ? this.distributionProfileService.update(payload.id, payload) : this.distributionProfileService.create(payload);
        request.subscribe(() => {
            this.distributionDialogVisible = false;
            this.selectPartner(this.selectedPartner);
        });
    }

    deleteDistributionProfile(profile: TenantPartnerDistributionProfile) {
        if (!profile.id) return;
        this.distributionProfileService.delete(profile.id).subscribe(() => this.selectPartner(this.selectedPartner));
    }
}
