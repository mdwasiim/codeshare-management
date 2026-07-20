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
import { AircraftCabinLayout } from '@features/masters/aircraft/cabin-layouts/models/cabin-layouts.model';
import { AircraftCabinLayoutService } from '@features/masters/aircraft/cabin-layouts/services/cabin-layouts.service';

@Component({ selector: 'cabin-layouts-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './cabin-layouts-form.page.html' })
export class AircraftCabinLayoutFormPage extends BaseCrudForm<AircraftCabinLayout> {
    private fb = inject(FormBuilder);
    private service = inject(AircraftCabinLayoutService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            layoutCode: ['', [Validators.required]],
            layoutName: [''],
            cabinClass: [''],
            seatCount: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: AircraftCabinLayout): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: AircraftCabinLayout) { return this.service.create(payload); }
    update(id: string, payload: AircraftCabinLayout) { return this.service.update(id, payload); }
}
