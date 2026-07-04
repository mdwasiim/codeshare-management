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
import { Airport } from '@features/masters/geography/airports/models/airports.model';
import { AirportService } from '@features/masters/geography/airports/services/airports.service';

@Component({ selector: 'airports-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './airports-form.page.html' })
export class AirportFormPage extends BaseCrudForm<Airport> {
    private fb = inject(FormBuilder);
    private service = inject(AirportService);
    readonly recordStatusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' },
        { label: 'Draft', value: 'DRAFT' },
        { label: 'Archived', value: 'ARCHIVED' }
    ];
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            iataCode: ['', [Validators.required]],
            icaoCode: [''],
            airportName: ['', [Validators.required]],
            cityId: [''],
            countryId: [''],
            timezoneId: ['', [Validators.required]],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: Airport): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: Airport) { return this.service.create(payload); }
    update(id: string, payload: Airport) { return this.service.update(id, payload); }
}
