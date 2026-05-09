import {Component, inject, OnInit} from '@angular/core';

import {CommonModule} from '@angular/common';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';

import {InputTextModule} from 'primeng/inputtext';
import {ButtonModule} from 'primeng/button';
import {SelectModule} from 'primeng/select';

import {Organization} from "@features/settings/model/organization.model";
import {BaseCrudForm} from "@shared/components/base/base-form.component";
import {of} from "rxjs";
import {CsmFormSectionComponent} from '@shared/components/form-section/csm-form-section.component';

@Component({
    selector: 'organization-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule,
        SelectModule,
        CsmFormSectionComponent
    ],
    templateUrl: './organization-form.page.html'
})
export class OrganizationFormPage
    extends BaseCrudForm<Organization> {

    private fb = inject(FormBuilder);

    statusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' }
    ];

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
            name: ['', Validators.required],
            code: ['', Validators.required],
            status: ['ACTIVE', Validators.required]
        });
    }

    override patchForm(data: Organization): void {
        this.form.patchValue(data);
    }

    override fetchById(id: string) {
        return of({
            id,
            name: 'Qatar Airways',
            code: 'QR',
            status: 'ACTIVE' as 'ACTIVE' | 'INACTIVE'   // ✅ FIX
        });
    }

    override create(payload: any) {
        return of(true);
    }

    override update(id: string, payload: any) {
        return of(true);
    }

    // ✅ clean
    override submit(): void {
        super.submit();
    }
}
