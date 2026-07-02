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
import { AirlineAlias } from '@features/masters/airlines/airline-aliases/models/airline-aliases.model';
import { AirlineAliasService } from '@features/masters/airlines/airline-aliases/services/airline-aliases.service';

@Component({ selector: 'airline-aliases-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './airline-aliases-form.page.html' })
export class AirlineAliasFormPage extends BaseCrudForm<AirlineAlias> {
    private fb = inject(FormBuilder);
    private service = inject(AirlineAliasService);
    readonly recordStatusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' },
        { label: 'Draft', value: 'DRAFT' },
        { label: 'Archived', value: 'ARCHIVED' }
    ];
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            aliasCode: ['', [Validators.required]],
            aliasName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true],
            airlineId: ['']
        });
    }
    patchForm(data: AirlineAlias): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: AirlineAlias) { return this.service.create(payload); }
    update(id: string, payload: AirlineAlias) { return this.service.update(id, payload); }
}
