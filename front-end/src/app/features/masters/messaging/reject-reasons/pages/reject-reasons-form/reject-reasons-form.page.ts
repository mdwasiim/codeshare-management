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
import { RejectReason } from '@features/masters/messaging/reject-reasons/models/reject-reasons.model';
import { RejectReasonService } from '@features/masters/messaging/reject-reasons/services/reject-reasons.service';

@Component({ selector: 'reject-reasons-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './reject-reasons-form.page.html' })
export class RejectReasonFormPage extends BaseCrudForm<RejectReason> {
    private fb = inject(FormBuilder);
    private service = inject(RejectReasonService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            reasonCode: ['', [Validators.required]],
            reasonName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: RejectReason): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: RejectReason) { return this.service.create(payload); }
    update(id: string, payload: RejectReason) { return this.service.update(id, payload); }
}
