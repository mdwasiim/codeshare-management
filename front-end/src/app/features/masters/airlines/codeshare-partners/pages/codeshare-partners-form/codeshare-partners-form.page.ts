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
import { CodesharePartner } from '@features/masters/airlines/codeshare-partners/models/codeshare-partners.model';
import { CodesharePartnerService } from '@features/masters/airlines/codeshare-partners/services/codeshare-partners.service';

@Component({ selector: 'codeshare-partners-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './codeshare-partners-form.page.html' })
export class CodesharePartnerFormPage extends BaseCrudForm<CodesharePartner> {
    private fb = inject(FormBuilder);
    private service = inject(CodesharePartnerService);
    readonly recordStatusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' },
        { label: 'Draft', value: 'DRAFT' },
        { label: 'Archived', value: 'ARCHIVED' }
    ];
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            airlineId: ['', [Validators.required]],
            partnerCode: [''],
            partnerName: [''],
            partnerType: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: CodesharePartner): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: CodesharePartner) { return this.service.create(payload); }
    update(id: string, payload: CodesharePartner) { return this.service.update(id, payload); }
}
