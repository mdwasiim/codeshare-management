import {Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';

import {CommonModule} from '@angular/common';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';

import {InputTextModule} from 'primeng/inputtext';
import {ButtonModule} from 'primeng/button';
import {SelectModule} from 'primeng/select';

import {Role} from '@features/access-management/iam/models/role.model';
import {RoleService} from '../../services/role.service';
import {BaseCrudForm} from '@shared/components/base/base-form.component';

import {TenantService} from '@features/access-management/iam/tenants/services/tenant.service';
import {Tenant} from '@features/access-management/iam/models/tenant.model';
import {CsmFormSectionComponent} from '@shared/components/form-section/csm-form-section.component';

@Component({
    selector: 'role-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule,
        SelectModule,
        CsmFormSectionComponent
    ],
    templateUrl: './role-form.page.html'
})
export class RoleFormPage
    extends BaseCrudForm<Role> {

    @Input() visible = false;
    @Output() visibleChange = new EventEmitter<boolean>();

    tenants: Tenant[] = [];

    private fb = inject(FormBuilder);
    private service = inject(RoleService);
    private tenantService = inject(TenantService);

    // =========================
    // Lifecycle
    // =========================

    override ngOnInit(): void {
        this.buildForm();

        this.tenantService.getAll().subscribe({
            next: res => this.tenants = res
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
