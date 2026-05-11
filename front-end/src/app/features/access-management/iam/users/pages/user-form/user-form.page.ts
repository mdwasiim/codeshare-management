import {Component, EventEmitter, inject, OnInit, Output} from '@angular/core';

import {CommonModule} from '@angular/common';
import {FormBuilder, ReactiveFormsModule} from '@angular/forms';

import {InputTextModule} from 'primeng/inputtext';
import {PasswordModule} from 'primeng/password';
import {CheckboxModule} from 'primeng/checkbox';
import {ButtonModule} from 'primeng/button';
import {SelectModule} from 'primeng/select';

import {UserService} from '@features/access-management/iam/users/services/user.service';
import {User} from '@features/access-management/iam/models/user.model';
import {BaseCrudForm} from '@shared/components/base/base-form.component';
import {Tenant} from "@features/access-management/iam/models/tenant.model";
import {TenantService} from "@features/access-management/iam/tenants/services/tenant.service";
import {CsmFormSectionComponent} from "@shared/components/form-section/csm-form-section.component";

@Component({
    selector: 'user-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        PasswordModule,
        CheckboxModule,
        SelectModule,
        ButtonModule,
        CsmFormSectionComponent
    ],
    templateUrl: './user-form.page.html'
})
export class UserFormPage
    extends BaseCrudForm<User> {

    // =========================
    // Dialog Inputs
    // =========================
    private fb = inject(FormBuilder);
    private service = inject(UserService);
    private tenantService = inject(TenantService);

    tenants: Tenant[] = [];

    // =========================
    // Lifecycle
    // =========================
    override ngOnInit(): void {
        super.ngOnInit();
        this.tenantService.getAll().subscribe({
            next: res => this.tenants = res
        });
    }
    // =========================
    // Form
    // =========================
    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],

            username: [''],
            email: [''],
            password: [''],

            firstName: [''],
            lastName: [''],

            enabled: [true],
            accountNonLocked: [true],
            accountNonExpired: [true],
            credentialsNonExpired: [true],

            lastLogin: [''],
            lastLoginProvider: [''],

            externalId: [''],

            authSource: ['INTERNAL'],
            recordStatus: ['ACTIVE'],

            tenantId: [''],
            roleIds: [[]]
        });
    }

    patchForm(data: User): void {
        this.form.patchValue({
            ...data,
            tenantId: data?.tenant?.id || ''
        });
    }

    fetchById(id: string) {
        return this.service.getById(id);
    }

    create(payload: User) {
        return this.service.create(this.mapToModel(payload));
    }

    update(id: string, payload: any) {
        return this.service.update(id, this.mapToModel(payload));
    }

    // =========================
    // Mapper
    // =========================
    private mapToModel(formValue: User): User {
        const payload: any = { ...formValue };

        if (payload.tenantId) {
            payload.tenant = { id: payload.tenantId };
        }
        delete payload.tenantId;

        if (!payload.authSource) {
            payload.authSource = 'LOCAL';
        }

        if (this.id && !payload.password) {
            delete payload.password;
        }

        return payload;
    }
}
