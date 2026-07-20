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
import { ScheduleChannel } from '@features/masters/schedule/schedule-channels/models/schedule-channels.model';
import { ScheduleChannelService } from '@features/masters/schedule/schedule-channels/services/schedule-channels.service';

@Component({ selector: 'schedule-channels-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './schedule-channels-form.page.html' })
export class ScheduleChannelFormPage extends BaseCrudForm<ScheduleChannel> {
    private fb = inject(FormBuilder);
    private service = inject(ScheduleChannelService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            channelCode: ['', [Validators.required]],
            channelName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: ScheduleChannel): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: ScheduleChannel) { return this.service.create(payload); }
    update(id: string, payload: ScheduleChannel) { return this.service.update(id, payload); }
}
