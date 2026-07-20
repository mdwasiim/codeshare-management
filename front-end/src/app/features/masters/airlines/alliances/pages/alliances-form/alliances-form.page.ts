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
import { Alliance } from '@features/masters/airlines/alliances/models/alliances.model';
import { AllianceService } from '@features/masters/airlines/alliances/services/alliances.service';

@Component({ selector: 'alliances-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './alliances-form.page.html' })
export class AllianceFormPage extends BaseCrudForm<Alliance> {
    private fb = inject(FormBuilder);
    private service = inject(AllianceService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            allianceCode: ['', [Validators.required]],
            allianceName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: Alliance): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: Alliance) { return this.service.create(payload); }
    update(id: string, payload: Alliance) { return this.service.update(id, payload); }
}
