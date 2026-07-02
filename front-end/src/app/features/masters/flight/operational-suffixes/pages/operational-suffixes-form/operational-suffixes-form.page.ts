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
import { OperationalSuffix } from '@features/masters/flight/operational-suffixes/models/operational-suffixes.model';
import { OperationalSuffixService } from '@features/masters/flight/operational-suffixes/services/operational-suffixes.service';

@Component({ selector: 'operational-suffixes-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './operational-suffixes-form.page.html' })
export class OperationalSuffixFormPage extends BaseCrudForm<OperationalSuffix> {
    private fb = inject(FormBuilder);
    private service = inject(OperationalSuffixService);
    readonly recordStatusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' },
        { label: 'Draft', value: 'DRAFT' },
        { label: 'Archived', value: 'ARCHIVED' }
    ];
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            suffixCode: ['', [Validators.required]],
            suffixName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: OperationalSuffix): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: OperationalSuffix) { return this.service.create(payload); }
    update(id: string, payload: OperationalSuffix) { return this.service.update(id, payload); }
}
