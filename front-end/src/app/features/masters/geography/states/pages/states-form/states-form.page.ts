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
import { State } from '@features/masters/geography/states/models/states.model';
import { StateService } from '@features/masters/geography/states/services/states.service';

@Component({ selector: 'states-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './states-form.page.html' })
export class StateFormPage extends BaseCrudForm<State> {
    private fb = inject(FormBuilder);
    private service = inject(StateService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            code: ['', [Validators.required]],
            name: ['', [Validators.required]],
            recordStatus: ['ACTIVE'],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true],
            countryId: ['']
        });
    }
    patchForm(data: State): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: State) { return this.service.create(payload); }
    update(id: string, payload: State) { return this.service.update(id, payload); }
}
