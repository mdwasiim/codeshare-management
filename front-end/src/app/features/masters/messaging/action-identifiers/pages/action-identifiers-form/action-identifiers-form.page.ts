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
import { ActionIdentifier } from '@features/masters/messaging/action-identifiers/models/action-identifiers.model';
import { ActionIdentifierService } from '@features/masters/messaging/action-identifiers/services/action-identifiers.service';

@Component({ selector: 'action-identifiers-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './action-identifiers-form.page.html' })
export class ActionIdentifierFormPage extends BaseCrudForm<ActionIdentifier> {
    private fb = inject(FormBuilder);
    private service = inject(ActionIdentifierService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            actionIdentifier: ['', [Validators.required]],
            actionName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: ActionIdentifier): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: ActionIdentifier) { return this.service.create(payload); }
    update(id: string, payload: ActionIdentifier) { return this.service.update(id, payload); }
}
