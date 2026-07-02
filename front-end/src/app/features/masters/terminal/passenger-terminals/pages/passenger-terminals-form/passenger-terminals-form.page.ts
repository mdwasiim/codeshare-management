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
import { PassengerTerminal } from '@features/masters/terminal/passenger-terminals/models/passenger-terminals.model';
import { PassengerTerminalService } from '@features/masters/terminal/passenger-terminals/services/passenger-terminals.service';

@Component({ selector: 'passenger-terminals-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './passenger-terminals-form.page.html' })
export class PassengerTerminalFormPage extends BaseCrudForm<PassengerTerminal> {
    private fb = inject(FormBuilder);
    private service = inject(PassengerTerminalService);
    readonly recordStatusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' },
        { label: 'Draft', value: 'DRAFT' },
        { label: 'Archived', value: 'ARCHIVED' }
    ];
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            airportId: [''],
            terminalCode: ['', [Validators.required]],
            terminalName: [''],
            terminalType: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: PassengerTerminal): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: PassengerTerminal) { return this.service.create(payload); }
    update(id: string, payload: PassengerTerminal) { return this.service.update(id, payload); }
}
