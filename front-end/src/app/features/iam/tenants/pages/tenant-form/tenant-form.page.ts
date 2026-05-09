import {Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';

import {CommonModule} from '@angular/common';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';

import {InputTextModule} from 'primeng/inputtext';
import {ButtonModule} from 'primeng/button';
import {DialogModule} from 'primeng/dialog';
import {BaseCrudForm} from '@shared/components/base/base-form.component';
import {TenantService} from "../../services/tenant.service";
import {Tenant} from "@features/iam/models/tenant.model";
import {SelectModule} from "primeng/select";
import {CsmFormSectionComponent} from "@shared/components/form-section/csm-form-section.component";

@Component({
    selector: 'tenant-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule,
        DialogModule,
        SelectModule,CsmFormSectionComponent
    ],
    templateUrl: './tenant-form.page.html'
})
export class TenantFormPage extends BaseCrudForm<Tenant> {

    private fb = inject(FormBuilder);
    private service = inject(TenantService);

    statusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' },
        { label: 'Suspended', value: 'SUSPENDED' },
        { label: 'Trial', value: 'TRIAL' },
        { label: 'Expired', value: 'EXPIRED' }
    ];

    planOptions = [
        { label: 'Basic', value: 'BASIC' },
        { label: 'Premium', value: 'PREMIUM' },
        { label: 'Enterprise', value: 'ENTERPRISE' }
    ];


    override ngOnInit() {
        this.buildForm();
    }

    buildForm(): void {
        console.log('BUILD FORM CALLED');
        this.form = this.fb.group({
            id: [null],
            name: ['', Validators.required],
            code: ['', Validators.required],
            description: [''],
            status: ['ACTIVE'],
            plan: ['BASIC']
        });
    }

    patchForm(data: Tenant): void {
        this.form.patchValue({
            ...data
        });
    }

    fetchById(id: string) {
        return this.service.getById(id);
    }

    create(payload: Tenant) {
        return this.service.create(payload);
    }

    update(id: string, payload: Tenant) {
        return this.service.update(id, payload);
    }
}
