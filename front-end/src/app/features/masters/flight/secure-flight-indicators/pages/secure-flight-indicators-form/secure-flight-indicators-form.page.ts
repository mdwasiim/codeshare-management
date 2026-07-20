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
import { SecureFlightIndicator } from '@features/masters/flight/secure-flight-indicators/models/secure-flight-indicators.model';
import { SecureFlightIndicatorService } from '@features/masters/flight/secure-flight-indicators/services/secure-flight-indicators.service';

@Component({ selector: 'secure-flight-indicators-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './secure-flight-indicators-form.page.html' })
export class SecureFlightIndicatorFormPage extends BaseCrudForm<SecureFlightIndicator> {
    private fb = inject(FormBuilder);
    private service = inject(SecureFlightIndicatorService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            indicatorCode: ['', [Validators.required]],
            indicatorName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: SecureFlightIndicator): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: SecureFlightIndicator) { return this.service.create(payload); }
    update(id: string, payload: SecureFlightIndicator) { return this.service.update(id, payload); }
}
