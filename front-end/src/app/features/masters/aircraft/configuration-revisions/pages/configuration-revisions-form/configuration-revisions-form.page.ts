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
import { AircraftConfigurationRevision } from '@features/masters/aircraft/configuration-revisions/models/configuration-revisions.model';
import { AircraftConfigurationRevisionService } from '@features/masters/aircraft/configuration-revisions/services/configuration-revisions.service';

@Component({ selector: 'configuration-revisions-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './configuration-revisions-form.page.html' })
export class AircraftConfigurationRevisionFormPage extends BaseCrudForm<AircraftConfigurationRevision> {
    private fb = inject(FormBuilder);
    private service = inject(AircraftConfigurationRevisionService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            configurationId: ['', [Validators.required]],
            revisionCode: ['', [Validators.required]],
            revisionName: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            recordStatus: ['']
        });
    }
    patchForm(data: AircraftConfigurationRevision): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: AircraftConfigurationRevision) { return this.service.create(payload); }
    update(id: string, payload: AircraftConfigurationRevision) { return this.service.update(id, payload); }
}
