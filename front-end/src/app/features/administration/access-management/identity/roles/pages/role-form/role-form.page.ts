import { Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { Role } from '@features/administration/access-management/models/role.model';
import { RoleService } from '../../services/role.service';
import { BaseCrudForm } from '@shared/components/base/base-form.component';
import { AppFormSectionComponent } from '@shared/components/form-section/app-form-section.component';

@Component({
    selector: 'role-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, InputTextModule, ButtonModule, AppFormSectionComponent],
    templateUrl: './role-form.page.html'
})
export class RoleFormPage extends BaseCrudForm<Role> {
    @Input() visible = false;
    @Output() visibleChange = new EventEmitter<boolean>();

    private fb = inject(FormBuilder);
    private service = inject(RoleService);

    // =========================
    // Lifecycle
    // =========================

    override ngOnInit(): void {
        this.buildForm();
    }

    // =========================
    // Form
    // =========================

    override buildForm(): void {
        this.form = this.fb.group({
            id: [null],
            code: ['', Validators.required],
            name: ['', Validators.required],
            description: ['']
        });
    }

    override patchForm(data: Role): void {
        this.form.patchValue(data);
    }

    override fetchById(id: string) {
        return this.service.getById(id);
    }

    override create(payload: any) {
        return this.service.create(payload);
    }

    override update(id: string, payload: any) {
        return this.service.update(id, payload);
    }

    // ✅ CLEAN (no subscribe here)
    override submit(): void {
        super.submit();
    }
}

