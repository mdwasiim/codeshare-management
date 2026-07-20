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
import { Season } from '@features/masters/schedule/seasons/models/seasons.model';
import { SeasonService } from '@features/masters/schedule/seasons/services/seasons.service';

@Component({ selector: 'seasons-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './seasons-form.page.html' })
export class SeasonFormPage extends BaseCrudForm<Season> {
    private fb = inject(FormBuilder);
    private service = inject(SeasonService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            seasonCode: ['', [Validators.required]],
            seasonName: ['', [Validators.required]],
            seasonType: [''],
            startDate: [''],
            endDate: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: Season): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: Season) { return this.service.create(payload); }
    update(id: string, payload: Season) { return this.service.update(id, payload); }
}
