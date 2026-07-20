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
import { StandardMessageIdentifier } from '@features/masters/messaging/standard-message-identifiers/models/standard-message-identifiers.model';
import { StandardMessageIdentifierService } from '@features/masters/messaging/standard-message-identifiers/services/standard-message-identifiers.service';

@Component({ selector: 'standard-message-identifiers-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './standard-message-identifiers-form.page.html' })
export class StandardMessageIdentifierFormPage extends BaseCrudForm<StandardMessageIdentifier> {
    private fb = inject(FormBuilder);
    private service = inject(StandardMessageIdentifierService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            messageIdentifier: ['', [Validators.required]],
            messageName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: StandardMessageIdentifier): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: StandardMessageIdentifier) { return this.service.create(payload); }
    update(id: string, payload: StandardMessageIdentifier) { return this.service.update(id, payload); }
}
