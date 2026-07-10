import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { AppFormSectionComponent } from '@shared/components/form-section/app-form-section.component';
import { BaseCrudForm } from '@shared/components/base/base-form.component';
import { TenantPartner } from '@features/partner-management/tenant-partners/models/tenant-partner.model';
import { TenantPartnerService } from '@features/partner-management/tenant-partners/services/tenant-partners.service';

@Component({
    selector: 'tenant-partners-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent],
    templateUrl: './tenant-partners-form.page.html'
})
export class TenantPartnerFormPage extends BaseCrudForm<TenantPartner> {
    private fb = inject(FormBuilder);
    private service = inject(TenantPartnerService);

    readonly recordStatusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' },
        { label: 'Draft', value: 'DRAFT' },
        { label: 'Archived', value: 'ARCHIVED' }
    ];

    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            homeAirlineId: ['', [Validators.required]],
            partnerAirlineId: ['', [Validators.required]],
            agreementNumber: [''],
            agreementType: ['BILATERAL'],
            agreementStatus: ['ACTIVE'],
            recordStatus: ['ACTIVE'],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }

    patchForm(data: TenantPartner): void {
        this.form.patchValue(data);
    }

    fetchById(id: string) {
        return this.service.getById(id);
    }

    create(payload: TenantPartner) {
        return this.service.create(payload);
    }

    update(id: string, payload: TenantPartner) {
        return this.service.update(id, payload);
    }
}
