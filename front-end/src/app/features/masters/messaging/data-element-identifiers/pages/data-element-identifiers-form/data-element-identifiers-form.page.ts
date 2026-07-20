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
import { DataElementIdentifier } from '@features/masters/messaging/data-element-identifiers/models/data-element-identifiers.model';
import { DataElementIdentifierService } from '@features/masters/messaging/data-element-identifiers/services/data-element-identifiers.service';

@Component({ selector: 'data-element-identifiers-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './data-element-identifiers-form.page.html' })
export class DataElementIdentifierFormPage extends BaseCrudForm<DataElementIdentifier> {
    private fb = inject(FormBuilder);
    private service = inject(DataElementIdentifierService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            deiCode: ['', [Validators.required]],
            deiName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: DataElementIdentifier): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: DataElementIdentifier) { return this.service.create(payload); }
    update(id: string, payload: DataElementIdentifier) { return this.service.update(id, payload); }
}
