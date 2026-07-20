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
import { Timezone } from '@features/masters/geography/timezones/models/timezones.model';
import { TimezoneService } from '@features/masters/geography/timezones/services/timezones.service';

@Component({ selector: 'timezones-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './timezones-form.page.html' })
export class TimezoneFormPage extends BaseCrudForm<Timezone> {
    private fb = inject(FormBuilder);
    private service = inject(TimezoneService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            zoneId: ['', [Validators.required]],
            utcOffset: [''],
            isDstSupported: [false],
            recordStatus: ['ACTIVE'],
            effectiveFrom: [''],
            effectiveTo: ['']
        });
    }
    patchForm(data: Timezone): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: Timezone) { return this.service.create(payload); }
    update(id: string, payload: Timezone) { return this.service.update(id, payload); }
}
