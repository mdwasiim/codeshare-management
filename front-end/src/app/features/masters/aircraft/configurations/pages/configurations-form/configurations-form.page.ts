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
import { AircraftConfiguration } from '@features/masters/aircraft/configurations/models/configurations.model';
import { AircraftConfigurationService } from '@features/masters/aircraft/configurations/services/configurations.service';

@Component({ selector: 'configurations-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './configurations-form.page.html' })
export class AircraftConfigurationFormPage extends BaseCrudForm<AircraftConfiguration> {
    private fb = inject(FormBuilder);
    private service = inject(AircraftConfigurationService);
    readonly recordStatusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' },
        { label: 'Draft', value: 'DRAFT' },
        { label: 'Archived', value: 'ARCHIVED' }
    ];
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            configurationCode: ['', [Validators.required]],
            configurationName: [''],
            aircraftTypeId: [''],
            totalSeats: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: AircraftConfiguration): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: AircraftConfiguration) { return this.service.create(payload); }
    update(id: string, payload: AircraftConfiguration) { return this.service.update(id, payload); }
}
