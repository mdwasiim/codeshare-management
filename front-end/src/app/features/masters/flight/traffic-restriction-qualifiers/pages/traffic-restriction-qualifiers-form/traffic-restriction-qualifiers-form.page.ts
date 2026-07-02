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
import { TrafficRestrictionQualifier } from '@features/masters/flight/traffic-restriction-qualifiers/models/traffic-restriction-qualifiers.model';
import { TrafficRestrictionQualifierService } from '@features/masters/flight/traffic-restriction-qualifiers/services/traffic-restriction-qualifiers.service';

@Component({ selector: 'traffic-restriction-qualifiers-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './traffic-restriction-qualifiers-form.page.html' })
export class TrafficRestrictionQualifierFormPage extends BaseCrudForm<TrafficRestrictionQualifier> {
    private fb = inject(FormBuilder);
    private service = inject(TrafficRestrictionQualifierService);
    readonly recordStatusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' },
        { label: 'Draft', value: 'DRAFT' },
        { label: 'Archived', value: 'ARCHIVED' }
    ];
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            qualifierCode: ['', [Validators.required]],
            qualifierName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: TrafficRestrictionQualifier): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: TrafficRestrictionQualifier) { return this.service.create(payload); }
    update(id: string, payload: TrafficRestrictionQualifier) { return this.service.update(id, payload); }
}
