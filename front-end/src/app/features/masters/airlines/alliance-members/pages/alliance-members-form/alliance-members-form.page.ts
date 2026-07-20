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
import { AllianceMember } from '@features/masters/airlines/alliance-members/models/alliance-members.model';
import { AllianceMemberService } from '@features/masters/airlines/alliance-members/services/alliance-members.service';

@Component({ selector: 'alliance-members-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, InputTextModule, InputNumberModule, TextareaModule, SelectModule, CheckboxModule, ButtonModule, AppFormSectionComponent], templateUrl: './alliance-members-form.page.html' })
export class AllianceMemberFormPage extends BaseCrudForm<AllianceMember> {
    private fb = inject(FormBuilder);
    private service = inject(AllianceMemberService);
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            allianceId: ['', [Validators.required]],
            airlineId: ['', [Validators.required]],
            memberType: [''],
            recordStatus: [''],
            effectiveFrom: [''],
            effectiveTo: [''],
            active: [true]
        });
    }
    patchForm(data: AllianceMember): void { this.form.patchValue(data); }
    fetchById(id: string) { return this.service.getById(id); }
    create(payload: AllianceMember) { return this.service.create(payload); }
    update(id: string, payload: AllianceMember) { return this.service.update(id, payload); }
}
