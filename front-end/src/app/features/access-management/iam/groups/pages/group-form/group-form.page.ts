import { Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';

import { Group } from '@features/access-management/iam/models/group.model';
import { GroupService } from '../../services/group.service';
import { BaseCrudForm } from '@shared/components/base/base-form.component';
import { TenantService } from '@features/access-management/iam/tenants/services/tenant.service';
import { Tenant } from '@features/access-management/iam/models/tenant.model';
import { AppFormSectionComponent } from '@shared/components/form-section/app-form-section.component';

@Component({
    selector: 'group-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, InputTextModule, ButtonModule, SelectModule, AppFormSectionComponent],
    templateUrl: './group-form.page.html'
})
export class GroupFormPage extends BaseCrudForm<Group> {
    @Input() visible = false;
    @Output() visibleChange = new EventEmitter<boolean>();

    tenants: Tenant[] = [];

    private fb = inject(FormBuilder);
    private service = inject(GroupService);
    private tenantService = inject(TenantService);

    // =========================
    // Lifecycle
    // =========================

    override ngOnInit(): void {
        this.buildForm();
        this.tenantService.getAll().subscribe({
            next: (res) => (this.tenants = res)
        });
    }

    // =========================
    // Form
    // =========================

    override buildForm(): void {
        this.form = this.fb.group({
            id: [null],
            code: ['', Validators.required],
            name: ['', Validators.required],
            description: [''],
            tenantId: [null, Validators.required]
        });
    }

    override patchForm(data: Group): void {
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

    // ✅ no subscription here
    override submit(): void {
        super.submit();
    }
}
