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
import { ElectronicTicketIndicator } from '@features/masters/flight/electronic-ticket-indicators/models/electronic-ticket-indicators.model';
import { ElectronicTicketIndicatorService } from '@features/masters/flight/electronic-ticket-indicators/services/electronic-ticket-indicators.service';

@Component({ selector: 'electronic-ticket-indicators-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './electronic-ticket-indicators-form.page.html' })
export class ElectronicTicketIndicatorFormPage extends BaseCrudForm<ElectronicTicketIndicator> {
    private fb = inject(FormBuilder);
    private service = inject(ElectronicTicketIndicatorService);
    readonly recordStatusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' },
        { label: 'Draft', value: 'DRAFT' },
        { label: 'Archived', value: 'ARCHIVED' }
    ];
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
    patchForm(data: ElectronicTicketIndicator): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: ElectronicTicketIndicator) { return this.service.create(payload); }
    update(id: string, payload: ElectronicTicketIndicator) { return this.service.update(id, payload); }
}
