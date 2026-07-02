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
import { ScheduleCategory } from '@features/masters/schedule/schedule-categories/models/schedule-categories.model';
import { ScheduleCategoryService } from '@features/masters/schedule/schedule-categories/services/schedule-categories.service';

@Component({ selector: 'schedule-categories-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './schedule-categories-form.page.html' })
export class ScheduleCategoryFormPage extends BaseCrudForm<ScheduleCategory> {
    private fb = inject(FormBuilder);
    private service = inject(ScheduleCategoryService);
    readonly recordStatusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' },
        { label: 'Draft', value: 'DRAFT' },
        { label: 'Archived', value: 'ARCHIVED' }
    ];
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            categoryCode: ['', [Validators.required]],
            categoryName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: ScheduleCategory): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: ScheduleCategory) { return this.service.create(payload); }
    update(id: string, payload: ScheduleCategory) { return this.service.update(id, payload); }
}
