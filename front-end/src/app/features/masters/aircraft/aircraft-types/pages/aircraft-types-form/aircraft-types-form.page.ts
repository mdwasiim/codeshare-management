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
import { AircraftType } from '@features/masters/aircraft/aircraft-types/models/aircraft-types.model';
import { AircraftTypeService } from '@features/masters/aircraft/aircraft-types/services/aircraft-types.service';

@Component({ selector: 'aircraft-types-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './aircraft-types-form.page.html' })
export class AircraftTypeFormPage extends BaseCrudForm<AircraftType> {
    private fb = inject(FormBuilder);
    private service = inject(AircraftTypeService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            manufacturer: [''],
            modelCode: ['', [Validators.required]],
            icaoCode: [''],
            iataCode: [''],
            engineType: [''],
            typicalSeatCapacity: [''],
            maxRangeKm: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: AircraftType): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: AircraftType) { return this.service.create(payload); }
    update(id: string, payload: AircraftType) { return this.service.update(id, payload); }
}
