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
import { TrafficRestriction } from '@features/masters/flight/traffic-restrictions/models/traffic-restrictions.model';
import { TrafficRestrictionService } from '@features/masters/flight/traffic-restrictions/services/traffic-restrictions.service';

@Component({ selector: 'traffic-restrictions-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './traffic-restrictions-form.page.html' })
export class TrafficRestrictionFormPage extends BaseCrudForm<TrafficRestriction> {
    private fb = inject(FormBuilder);
    private service = inject(TrafficRestrictionService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            restrictionCode: ['', [Validators.required]],
            restrictionName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: TrafficRestriction): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: TrafficRestriction) { return this.service.create(payload); }
    update(id: string, payload: TrafficRestriction) { return this.service.update(id, payload); }
}
