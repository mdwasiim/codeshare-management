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
import { AirlineContact } from '@features/masters/airlines/airline-contacts/models/airline-contacts.model';
import { AirlineContactService } from '@features/masters/airlines/airline-contacts/services/airline-contacts.service';

@Component({ selector: 'airline-contacts-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './airline-contacts-form.page.html' })
export class AirlineContactFormPage extends BaseCrudForm<AirlineContact> {
    private fb = inject(FormBuilder);
    private service = inject(AirlineContactService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            airlineId: [''],
            contactName: ['', [Validators.required]],
            contactType: [''],
            email: [''],
            phone: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: AirlineContact): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: AirlineContact) { return this.service.create(payload); }
    update(id: string, payload: AirlineContact) { return this.service.update(id, payload); }
}
