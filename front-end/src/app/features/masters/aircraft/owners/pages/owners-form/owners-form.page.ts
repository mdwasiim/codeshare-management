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
import { AircraftOwner } from '@features/masters/aircraft/owners/models/owners.model';
import { AircraftOwnerService } from '@features/masters/aircraft/owners/services/owners.service';

@Component({ selector: 'owners-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './owners-form.page.html' })
export class AircraftOwnerFormPage extends BaseCrudForm<AircraftOwner> {
    private fb = inject(FormBuilder);
    private service = inject(AircraftOwnerService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            ownerCode: ['', [Validators.required]],
            ownerName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: AircraftOwner): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: AircraftOwner) { return this.service.create(payload); }
    update(id: string, payload: AircraftOwner) { return this.service.update(id, payload); }
}
