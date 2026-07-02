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
import { ScheduleStatus } from '@features/masters/schedule/schedule-statuses/models/schedule-statuses.model';
import { ScheduleStatusService } from '@features/masters/schedule/schedule-statuses/services/schedule-statuses.service';

@Component({ selector: 'schedule-statuses-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './schedule-statuses-form.page.html' })
export class ScheduleStatusFormPage extends BaseCrudForm<ScheduleStatus> {
    private fb = inject(FormBuilder);
    private service = inject(ScheduleStatusService);
    readonly recordStatusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' },
        { label: 'Draft', value: 'DRAFT' },
        { label: 'Archived', value: 'ARCHIVED' }
    ];
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            statusCode: ['', [Validators.required]],
            statusName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: ScheduleStatus): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: ScheduleStatus) { return this.service.create(payload); }
    update(id: string, payload: ScheduleStatus) { return this.service.update(id, payload); }
}
