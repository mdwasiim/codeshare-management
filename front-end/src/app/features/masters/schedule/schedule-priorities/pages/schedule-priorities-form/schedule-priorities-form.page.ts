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
import { SchedulePriority } from '@features/masters/schedule/schedule-priorities/models/schedule-priorities.model';
import { SchedulePriorityService } from '@features/masters/schedule/schedule-priorities/services/schedule-priorities.service';

@Component({ selector: 'schedule-priorities-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './schedule-priorities-form.page.html' })
export class SchedulePriorityFormPage extends BaseCrudForm<SchedulePriority> {
    private fb = inject(FormBuilder);
    private service = inject(SchedulePriorityService);
    readonly recordStatusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' },
        { label: 'Draft', value: 'DRAFT' },
        { label: 'Archived', value: 'ARCHIVED' }
    ];
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            priorityCode: ['', [Validators.required]],
            priorityName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: SchedulePriority): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: SchedulePriority) { return this.service.create(payload); }
    update(id: string, payload: SchedulePriority) { return this.service.update(id, payload); }
}
