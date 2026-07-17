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

import {
    AgreementCategory,
    InventorySharingType,
    PartnerType,
    TenantPartnerProfile
} from '@features/administration/tenant-management/models/tenant-partner-profile.model';
import { TenantPartnerService } from '../../services/tenant-partner.service';
import { TenantPartnerProfileService } from '../../services/tenant-partner-profile.service';
import {
    AGREEMENT_CATEGORY_OPTIONS,
    INVENTORY_SHARING_OPTIONS,
    PARTNER_TYPE_OPTIONS,
    RECORD_STATUS_OPTIONS,
    TenantPartnerOption,
    normalizeOptionalText,
    toDateInputValue,
    toTenantPartnerOptions
} from '../../shared/tenant-partner-profile.helpers';

@Component({
    selector: 'tenant-partner-profile-form',
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
    templateUrl: './tenant-partner-profile-form.page.html'
})
export class TenantPartnerProfileFormPage extends BaseCrudForm<TenantPartnerProfile> {
    private readonly fb = inject(FormBuilder);
    private readonly service = inject(TenantPartnerProfileService);
    private readonly tenantPartnerService = inject(TenantPartnerService);

    readonly recordStatusOptions = [...RECORD_STATUS_OPTIONS];
    readonly partnerTypeOptions = [...PARTNER_TYPE_OPTIONS];
    readonly agreementCategoryOptions = [...AGREEMENT_CATEGORY_OPTIONS];
    readonly inventorySharingOptions = [...INVENTORY_SHARING_OPTIONS];

    partnerOptions: TenantPartnerOption[] = [];

    override ngOnInit(): void {
        super.ngOnInit();
        this.loadPartnerOptions();
    }

    override buildForm(): void {
        this.form = this.fb.group({
            id: [null as number | null],
            partnerId: [null as number | null, Validators.required],
            profileCode: ['', [Validators.required, Validators.maxLength(100)]],
            profileName: ['', [Validators.required, Validators.maxLength(200)]],
            partnerType: [null as PartnerType | null],
            agreementCategory: [null as AgreementCategory | null],
            inventorySharingType: [null as InventorySharingType | null],
            priority: [null as number | null],
            displayOrder: [null as number | null],
            autoAcceptScheduleChanges: [false],
            prorationApplicable: [false],
            eTicketAllowed: [false],
            active: [true],
            recordStatus: ['ACTIVE'],
            effectiveFrom: [''],
            effectiveTo: [''],
            description: ['', Validators.maxLength(500)],
            remarks: ['', Validators.maxLength(1000)]
        });
    }

    override patchForm(data: TenantPartnerProfile): void {
        this.form.patchValue({
            ...data,
            effectiveFrom: toDateInputValue(data.effectiveFrom),
            effectiveTo: toDateInputValue(data.effectiveTo)
        });
    }

    override fetchById(id: string | number) {
        return this.service.getById(id);
    }

    override create(payload: TenantPartnerProfile) {
        return this.service.create(this.toPayload(payload));
    }

    override update(id: string | number, payload: TenantPartnerProfile) {
        return this.service.update(id, this.toPayload(payload));
    }

    private loadPartnerOptions(): void {
        this.tenantPartnerService.getAll().subscribe((partners) => {
            this.partnerOptions = toTenantPartnerOptions(partners ?? []);
        });
    }

    private toPayload(value: TenantPartnerProfile): TenantPartnerProfile {
        return {
            ...value,
            profileCode: normalizeOptionalText(value.profileCode)?.toUpperCase(),
            profileName: normalizeOptionalText(value.profileName),
            description: normalizeOptionalText(value.description),
            remarks: normalizeOptionalText(value.remarks),
            effectiveFrom: value.effectiveFrom || undefined,
            effectiveTo: value.effectiveTo || undefined,
            active: !!value.active,
            autoAcceptScheduleChanges: !!value.autoAcceptScheduleChanges,
            prorationApplicable: !!value.prorationApplicable,
            eTicketAllowed: !!value.eTicketAllowed
        };
    }
}
