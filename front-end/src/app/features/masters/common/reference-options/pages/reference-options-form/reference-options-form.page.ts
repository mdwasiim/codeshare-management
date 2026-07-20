import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';

import { BaseCrudForm } from '@shared/components/base/base-form.component';
import { AppFormSectionComponent } from '@shared/components/form-section/app-form-section.component';
import { CommonReferenceOption } from '@features/masters/common/reference-options/models/reference-options.model';
import { CommonReferenceOptionService } from '@features/masters/common/reference-options/services/reference-options.service';

@Component({
    selector: 'common-reference-option-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent],
    templateUrl: './reference-options-form.page.html'
})
export class CommonReferenceOptionFormPage extends BaseCrudForm<CommonReferenceOption> {
    private fb = inject(FormBuilder);
    private service = inject(CommonReferenceOptionService);

    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            categoryCode: ['', [Validators.required, Validators.maxLength(100)]],
            value: ['', [Validators.required, Validators.maxLength(100)]],
            label: ['', [Validators.required, Validators.maxLength(200)]],
            description: ['', Validators.maxLength(500)],
            displayOrder: [null as number | null],
            recordStatus: ['ACTIVE'],
            active: [true]
        });
    }

    patchForm(data: CommonReferenceOption): void {
        this.form.patchValue(data);
    }

    fetchById(id: string | number) {
        return this.service.getById(id);
    }

    create(payload: CommonReferenceOption) {
        return this.service.create(this.toPayload(payload));
    }

    update(id: string | number, payload: CommonReferenceOption) {
        return this.service.update(id, this.toPayload(payload));
    }

    private toPayload(payload: CommonReferenceOption): CommonReferenceOption {
        return {
            ...payload,
            categoryCode: payload.categoryCode?.trim().toUpperCase(),
            value: payload.value?.trim().toUpperCase(),
            label: payload.label?.trim(),
            description: payload.description?.trim() || undefined,
            active: payload.active !== false
        };
    }
}
