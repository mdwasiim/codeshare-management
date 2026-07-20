import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { AppFormSectionComponent } from '@shared/components/form-section/app-form-section.component';
import { BaseCrudForm } from '@shared/components/base/base-form.component';
import { TimezoneDls } from '@features/masters/geography/timezone-dls/models/timezone-dls.model';
import { TimezoneDlsService } from '@features/masters/geography/timezone-dls/services/timezone-dls.service';

@Component({ selector: 'timezone-dls-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, SelectModule, ButtonModule, AppFormSectionComponent], templateUrl: './timezone-dls-form.page.html' })
export class TimezoneDlsFormPage extends BaseCrudForm<TimezoneDls> {
    private fb = inject(FormBuilder);
    private service = inject(TimezoneDlsService);

    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            timezoneId: ['', [Validators.required]],
            dstStart: ['', [Validators.required]],
            dstEnd: ['', [Validators.required]],
            dstOffsetMinutes: [60, [Validators.required, Validators.min(1)]],
            effectiveFrom: ['', [Validators.required]],
            effectiveTo: [''],
            recordStatus: ['ACTIVE']
        });
    }

    patchForm(data: TimezoneDls): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: TimezoneDls) { return this.service.create(payload); }
    update(id: string, payload: TimezoneDls) { return this.service.update(id, payload); }
}
