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
import { AirlineBusinessRole } from '@features/masters/airlines/airline-business-roles/models/airline-business-roles.model';
import { AirlineBusinessRoleService } from '@features/masters/airlines/airline-business-roles/services/airline-business-roles.service';

@Component({ selector: 'airline-business-roles-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './airline-business-roles-form.page.html' })
export class AirlineBusinessRoleFormPage extends BaseCrudForm<AirlineBusinessRole> {
    private fb = inject(FormBuilder);
    private service = inject(AirlineBusinessRoleService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            roleCode: ['', [Validators.required]],
            roleName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: AirlineBusinessRole): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: AirlineBusinessRole) { return this.service.create(payload); }
    update(id: string, payload: AirlineBusinessRole) { return this.service.update(id, payload); }
}
