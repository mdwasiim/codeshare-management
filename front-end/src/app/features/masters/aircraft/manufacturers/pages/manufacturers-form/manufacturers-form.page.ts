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
import { AircraftManufacturer } from '@features/masters/aircraft/manufacturers/models/manufacturers.model';
import { AircraftManufacturerService } from '@features/masters/aircraft/manufacturers/services/manufacturers.service';

@Component({ selector: 'manufacturers-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './manufacturers-form.page.html' })
export class AircraftManufacturerFormPage extends BaseCrudForm<AircraftManufacturer> {
    private fb = inject(FormBuilder);
    private service = inject(AircraftManufacturerService);
    readonly recordStatusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' },
        { label: 'Draft', value: 'DRAFT' },
        { label: 'Archived', value: 'ARCHIVED' }
    ];
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            manufacturerCode: ['', [Validators.required]],
            manufacturerName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: AircraftManufacturer): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: AircraftManufacturer) { return this.service.create(payload); }
    update(id: string, payload: AircraftManufacturer) { return this.service.update(id, payload); }
}
