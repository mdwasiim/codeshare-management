import { Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';

import { Permission } from '@features/access-management/models/permission.model';
import { PermissionApiService } from '@features/access-management/authorization/permissions/services/permission-api.service';
import { TenantService } from '@features/access-management/identity/tenants/services/tenant.service';

import { BaseCrudForm } from '@shared/components/base/base-form.component';
import { AppFormSectionComponent } from '@shared/components/form-section/app-form-section.component';

@Component({
    selector: 'permission-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, InputTextModule, ButtonModule, SelectModule, AppFormSectionComponent],
    templateUrl: './permission-form.page.html'
})
export class PermissionFormPage extends BaseCrudForm<Permission> {
    @Input() visible = false;
    @Output() visibleChange = new EventEmitter<boolean>();

    private fb = inject(FormBuilder);
    private service = inject(PermissionApiService);
    private tenantService = inject(TenantService);

    tenants: any[] = [];
    domains: any[] = [];
    actions: any[] = [];

    override ngOnInit(): void {
        this.buildForm();

        this.tenantService.getAll().subscribe({
            next: (res) => (this.tenants = res)
        });

        this.domains = [
            { name: 'User', code: 'USER' },
            { name: 'Group', code: 'GROUP' },
            { name: 'Permission', code: 'PERMISSION' }
        ];

        this.actions = [
            { name: 'Create', code: 'CREATE' },
            { name: 'Read', code: 'READ' },
            { name: 'Update', code: 'UPDATE' },
            { name: 'Delete', code: 'DELETE' }
        ];

        this.form.valueChanges.subscribe((val) => {
            if (val.domain && val.action) {
                this.form.patchValue(
                    {
                        name: `${val.domain} ${val.action}`
                    },
                    { emitEvent: false }
                );
            }
        });
    }

    override buildForm(): void {
        this.form = this.fb.group({
            id: [null],
            name: ['', Validators.required],
            domain: ['', Validators.required],
            action: ['', Validators.required],
            description: [''],
            tenantId: ['', Validators.required]
        });
    }

    override patchForm(data: Permission): void {
        this.form.patchValue(data);
    }

    override fetchById(id: string) {
        return this.service.getById(id);
    }

    override create(payload: any) {
        payload.domain = payload.domain?.toUpperCase();
        payload.action = payload.action?.toUpperCase();
        return this.service.create(payload);
    }

    override update(id: string, payload: any) {
        payload.domain = payload.domain?.toUpperCase();
        payload.action = payload.action?.toUpperCase();
        return this.service.update(id, payload);
    }
}
