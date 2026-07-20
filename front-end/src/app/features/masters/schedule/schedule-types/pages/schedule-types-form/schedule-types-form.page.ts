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
import { ScheduleType } from '@features/masters/schedule/schedule-types/models/schedule-types.model';
import { ScheduleTypeService } from '@features/masters/schedule/schedule-types/services/schedule-types.service';

@Component({ selector: 'schedule-types-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './schedule-types-form.page.html' })
export class ScheduleTypeFormPage extends BaseCrudForm<ScheduleType> {
    private fb = inject(FormBuilder);
    private service = inject(ScheduleTypeService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            typeCode: ['', [Validators.required]],
            typeName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: ScheduleType): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: ScheduleType) { return this.service.create(payload); }
    update(id: string, payload: ScheduleType) { return this.service.update(id, payload); }
}
