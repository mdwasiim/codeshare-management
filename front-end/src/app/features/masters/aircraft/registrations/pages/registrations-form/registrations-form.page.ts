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
import { AircraftRegistration } from '@features/masters/aircraft/registrations/models/registrations.model';
import { AircraftRegistrationService } from '@features/masters/aircraft/registrations/services/registrations.service';

@Component({ selector: 'registrations-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './registrations-form.page.html' })
export class AircraftRegistrationFormPage extends BaseCrudForm<AircraftRegistration> {
    private fb = inject(FormBuilder);
    private service = inject(AircraftRegistrationService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            registrationNumber: ['', [Validators.required]],
            aircraftTypeId: [''],
            ownerId: [''],
            active: [true],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
        });
    }
    patchForm(data: AircraftRegistration): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: AircraftRegistration) { return this.service.create(payload); }
    update(id: string, payload: AircraftRegistration) { return this.service.update(id, payload); }
}
