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
import { FlightFrequency } from '@features/masters/flight/flight-frequencies/models/flight-frequencies.model';
import { FlightFrequencyService } from '@features/masters/flight/flight-frequencies/services/flight-frequencies.service';

@Component({ selector: 'flight-frequencies-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './flight-frequencies-form.page.html' })
export class FlightFrequencyFormPage extends BaseCrudForm<FlightFrequency> {
    private fb = inject(FormBuilder);
    private service = inject(FlightFrequencyService);
    readonly recordStatusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' },
        { label: 'Draft', value: 'DRAFT' },
        { label: 'Archived', value: 'ARCHIVED' }
    ];
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            frequencyCode: ['', [Validators.required]],
            frequencyName: [''],
            daysOfOperation: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: FlightFrequency): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: FlightFrequency) { return this.service.create(payload); }
    update(id: string, payload: FlightFrequency) { return this.service.update(id, payload); }
}
