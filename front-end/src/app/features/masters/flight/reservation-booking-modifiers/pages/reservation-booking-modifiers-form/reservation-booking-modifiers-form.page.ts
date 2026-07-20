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
import { ReservationBookingModifier } from '@features/masters/flight/reservation-booking-modifiers/models/reservation-booking-modifiers.model';
import { ReservationBookingModifierService } from '@features/masters/flight/reservation-booking-modifiers/services/reservation-booking-modifiers.service';

@Component({ selector: 'reservation-booking-modifiers-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './reservation-booking-modifiers-form.page.html' })
export class ReservationBookingModifierFormPage extends BaseCrudForm<ReservationBookingModifier> {
    private fb = inject(FormBuilder);
    private service = inject(ReservationBookingModifierService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            modifierCode: ['', [Validators.required]],
            modifierName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: ReservationBookingModifier): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: ReservationBookingModifier) { return this.service.create(payload); }
    update(id: string, payload: ReservationBookingModifier) { return this.service.update(id, payload); }
}
