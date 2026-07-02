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
import { AirlineCarrier } from '@features/masters/airlines/airline-carriers/models/airline-carriers.model';
import { AirlineCarrierService } from '@features/masters/airlines/airline-carriers/services/airline-carriers.service';

@Component({ selector: 'airline-carriers-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './airline-carriers-form.page.html' })
export class AirlineCarrierFormPage extends BaseCrudForm<AirlineCarrier> {
    private fb = inject(FormBuilder);
    private service = inject(AirlineCarrierService);
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
            iataNumericCode: [''],
            legalName: ['', [Validators.required]],
            commercialName: [''],
            displayName: [''],
            callsign: [''],
            website: [''],
            email: [''],
            phone: [''],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: AirlineCarrier): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: AirlineCarrier) { return this.service.create(payload); }
    update(id: string, payload: AirlineCarrier) { return this.service.update(id, payload); }
}
