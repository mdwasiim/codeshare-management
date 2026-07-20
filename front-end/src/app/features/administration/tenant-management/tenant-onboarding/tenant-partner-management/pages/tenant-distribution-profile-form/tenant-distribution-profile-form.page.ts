import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { TextareaModule } from 'primeng/textarea';
import { SelectModule } from 'primeng/select';
import { CheckboxModule } from 'primeng/checkbox';
import { ButtonModule } from 'primeng/button';

import { BaseCrudForm } from '@shared/components/base/base-form.component';
import { AppFormSectionComponent } from '@shared/components/form-section/app-form-section.component';
import { MasterReferenceLookupService } from '@features/masters/shared/master-reference-lookup.service';

import {
    CommunicationProtocol,
    DistributionMessageType,
    DistributionMode,
    TenantPartnerDistributionProfile
} from '@features/administration/tenant-management/models/tenant-partner-profile.model';
import { TenantPartnerService } from '../../services/tenant-partner.service';
import { TenantPartnerDistributionProfileService } from '../../services/tenant-partner-distribution-profile.service';
import {
    SelectOption,
    TenantPartnerOption,
    normalizeOptionalText,
    toDateInputValue,
    toTenantPartnerOptions
} from '../../shared/tenant-partner-profile.helpers';

@Component({
    selector: 'tenant-distribution-profile-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        InputNumberModule,
        TextareaModule,
        SelectModule,
        CheckboxModule,
        ButtonModule,
        AppFormSectionComponent
    ],
    templateUrl: './tenant-distribution-profile-form.page.html'
})
export class TenantDistributionProfileFormPage extends BaseCrudForm<TenantPartnerDistributionProfile> {
    private readonly fb = inject(FormBuilder);
    private readonly service = inject(TenantPartnerDistributionProfileService);
    private readonly tenantPartnerService = inject(TenantPartnerService);
    private readonly masterLookup = inject(MasterReferenceLookupService);

    override recordStatusOptions: SelectOption[] = [];
    channelOptions: SelectOption<CommunicationProtocol>[] = [];
    distributionModeOptions: SelectOption<DistributionMode>[] = [];
    messageTypeOptions: SelectOption<DistributionMessageType>[] = [];

    partnerOptions: TenantPartnerOption[] = [];

    override ngOnInit(): void {
        super.ngOnInit();
        this.loadPartnerOptions();
        this.loadProfileReferenceOptions();
    }

    override buildForm(): void {
        this.form = this.fb.group({
            id: [null as number | null],
            partnerId: [null as number | null, Validators.required],
            profileCode: ['', [Validators.required, Validators.maxLength(100)]],
            profileName: ['', [Validators.required, Validators.maxLength(200)]],
            distributionChannel: [null as CommunicationProtocol | null],
            distributionMode: [null as DistributionMode | null],
            messageType: [null as DistributionMessageType | null],
            realTimeEnabled: [false],
            acknowledgementRequired: [false],
            retryEnabled: [false],
            retryCount: [null as number | null],
            active: [true],
            displayOrder: [null as number | null],
            recordStatus: ['ACTIVE'],
            effectiveFrom: [''],
            effectiveTo: [''],
            description: ['', Validators.maxLength(500)],
            remarks: ['', Validators.maxLength(1000)]
        });
    }

    override patchForm(data: TenantPartnerDistributionProfile): void {
        this.form.patchValue({
            ...data,
            effectiveFrom: toDateInputValue(data.effectiveFrom),
            effectiveTo: toDateInputValue(data.effectiveTo)
        });
    }

    override fetchById(id: string | number) {
        return this.service.getById(id);
    }

    override create(payload: TenantPartnerDistributionProfile) {
        return this.service.create(this.toPayload(payload));
    }

    override update(id: string | number, payload: TenantPartnerDistributionProfile) {
        return this.service.update(id, this.toPayload(payload));
    }

    private loadPartnerOptions(): void {
        this.tenantPartnerService.getAll().subscribe((partners) => {
            this.partnerOptions = toTenantPartnerOptions(partners ?? []);
        });
    }

    private loadProfileReferenceOptions(): void {
        this.masterLookup.getReferenceOptions('PARTNER_PROFILE_STATUS').subscribe((options) => {
            this.recordStatusOptions = options;
        });
        this.masterLookup.getReferenceOptions<CommunicationProtocol>('COMMUNICATION_PROTOCOL').subscribe((options) => {
            this.channelOptions = options;
        });
        this.masterLookup.getReferenceOptions<DistributionMode>('DISTRIBUTION_MODE').subscribe((options) => {
            this.distributionModeOptions = options;
        });
        this.masterLookup.getReferenceOptions<DistributionMessageType>('DISTRIBUTION_MESSAGE_TYPE').subscribe((options) => {
            this.messageTypeOptions = options;
        });
    }

    private toPayload(value: TenantPartnerDistributionProfile): TenantPartnerDistributionProfile {
        return {
            ...value,
            profileCode: normalizeOptionalText(value.profileCode)?.toUpperCase(),
            profileName: normalizeOptionalText(value.profileName),
            description: normalizeOptionalText(value.description),
            remarks: normalizeOptionalText(value.remarks),
            effectiveFrom: value.effectiveFrom || undefined,
            effectiveTo: value.effectiveTo || undefined,
            realTimeEnabled: !!value.realTimeEnabled,
            acknowledgementRequired: !!value.acknowledgementRequired,
            retryEnabled: !!value.retryEnabled,
            active: !!value.active
        };
    }
}
