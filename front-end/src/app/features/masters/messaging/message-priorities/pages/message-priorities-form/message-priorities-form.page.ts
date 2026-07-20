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
import { MessagePriority } from '@features/masters/messaging/message-priorities/models/message-priorities.model';
import { MessagePriorityService } from '@features/masters/messaging/message-priorities/services/message-priorities.service';

@Component({ selector: 'message-priorities-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './message-priorities-form.page.html' })
export class MessagePriorityFormPage extends BaseCrudForm<MessagePriority> {
    private fb = inject(FormBuilder);
    private service = inject(MessagePriorityService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            priorityCode: ['', [Validators.required]],
            priorityName: ['', [Validators.required]],
            description: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: MessagePriority): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: MessagePriority) { return this.service.create(payload); }
    update(id: string, payload: MessagePriority) { return this.service.update(id, payload); }
}
