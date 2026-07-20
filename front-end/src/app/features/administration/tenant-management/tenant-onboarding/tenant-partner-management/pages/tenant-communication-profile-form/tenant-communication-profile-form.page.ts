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
    AuthenticationType,
    CommunicationProtocol,
    MessageFormat,
    TenantPartnerCommunicationProfile,
    TransportType
} from '@features/administration/tenant-management/models/tenant-partner-profile.model';
import { TenantPartnerService } from '../../services/tenant-partner.service';
import { TenantPartnerCommunicationProfileService } from '../../services/tenant-partner-communication-profile.service';
import {
    SelectOption,
    TenantPartnerOption,
    normalizeOptionalText,
    toDateInputValue,
    toTenantPartnerOptions,
} from '../../shared/tenant-partner-profile.helpers';

@Component({
    selector: 'tenant-communication-profile-form',
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
    templateUrl: './tenant-communication-profile-form.page.html'
})
export class TenantCommunicationProfileFormPage extends BaseCrudForm<TenantPartnerCommunicationProfile> {
    private readonly fb = inject(FormBuilder);
    private readonly service = inject(TenantPartnerCommunicationProfileService);
    private readonly tenantPartnerService = inject(TenantPartnerService);
    private readonly masterLookup = inject(MasterReferenceLookupService);

    override recordStatusOptions: SelectOption[] = [];
    protocolOptions: SelectOption<CommunicationProtocol>[] = [];
    transportOptions: SelectOption<TransportType>[] = [];
    messageFormatOptions: SelectOption<MessageFormat>[] = [];
    authenticationTypeOptions: SelectOption<AuthenticationType>[] = [];

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
            protocol: [null as CommunicationProtocol | null],
            transportType: [null as TransportType | null],
            messageFormat: [null as MessageFormat | null],
            authenticationType: [null as AuthenticationType | null],
            endpointUrl: ['', Validators.maxLength(500)],
            username: ['', Validators.maxLength(200)],
            credentialAlias: ['', Validators.maxLength(200)],
            connectionTimeout: [null as number | null],
            readTimeout: [null as number | null],
            retryCount: [null as number | null],
            compressionEnabled: [false],
            encryptionEnabled: [false],
            active: [true],
            displayOrder: [null as number | null],
            recordStatus: ['ACTIVE'],
            effectiveFrom: [''],
            effectiveTo: [''],
            description: ['', Validators.maxLength(500)],
            remarks: ['', Validators.maxLength(1000)]
        });
    }

    override patchForm(data: TenantPartnerCommunicationProfile): void {
        this.form.patchValue({
            ...data,
            effectiveFrom: toDateInputValue(data.effectiveFrom),
            effectiveTo: toDateInputValue(data.effectiveTo)
        });
    }

    override fetchById(id: string | number) {
        return this.service.getById(id);
    }

    override create(payload: TenantPartnerCommunicationProfile) {
        return this.service.create(this.toPayload(payload));
    }

    override update(id: string | number, payload: TenantPartnerCommunicationProfile) {
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
            this.protocolOptions = options;
        });
        this.masterLookup.getReferenceOptions<TransportType>('TRANSPORT_TYPE').subscribe((options) => {
            this.transportOptions = options;
        });
        this.masterLookup.getReferenceOptions<MessageFormat>('MESSAGE_FORMAT').subscribe((options) => {
            this.messageFormatOptions = options;
        });
        this.masterLookup.getReferenceOptions<AuthenticationType>('AUTHENTICATION_TYPE').subscribe((options) => {
            this.authenticationTypeOptions = options;
        });
    }

    private toPayload(value: TenantPartnerCommunicationProfile): TenantPartnerCommunicationProfile {
        return {
            ...value,
            profileCode: normalizeOptionalText(value.profileCode)?.toUpperCase(),
            profileName: normalizeOptionalText(value.profileName),
            endpointUrl: normalizeOptionalText(value.endpointUrl),
            username: normalizeOptionalText(value.username),
            credentialAlias: normalizeOptionalText(value.credentialAlias),
            description: normalizeOptionalText(value.description),
            remarks: normalizeOptionalText(value.remarks),
            effectiveFrom: value.effectiveFrom || undefined,
            effectiveTo: value.effectiveTo || undefined,
            compressionEnabled: !!value.compressionEnabled,
            encryptionEnabled: !!value.encryptionEnabled,
            active: !!value.active
        };
    }
}
