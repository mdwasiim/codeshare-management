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
import { ReservationBookingDesignator } from '@features/masters/flight/reservation-booking-designators/models/reservation-booking-designators.model';
import { ReservationBookingDesignatorService } from '@features/masters/flight/reservation-booking-designators/services/reservation-booking-designators.service';

@Component({ selector: 'reservation-booking-designators-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './reservation-booking-designators-form.page.html' })
export class ReservationBookingDesignatorFormPage extends BaseCrudForm<ReservationBookingDesignator> {
    private fb = inject(FormBuilder);
    private service = inject(ReservationBookingDesignatorService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            rbdCode: ['', [Validators.required]],
            rbdName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: ReservationBookingDesignator): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: ReservationBookingDesignator) { return this.service.create(payload); }
    update(id: string, payload: ReservationBookingDesignator) { return this.service.update(id, payload); }
}
