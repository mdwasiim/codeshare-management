import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select'; // PrimeNG v17+

import { BaseFormComponent } from '@core/base/base-form.component';
import {Organization} from "@features/settings/model/organization.model";

@Component({
    selector: 'organization-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule,
        SelectModule
    ],
    templateUrl: './organization-form.page.html'
})
export class OrganizationFormPage extends BaseFormComponent<Organization> implements OnInit {

    private fb = inject(FormBuilder);
    // 🔸 later: private service = inject(OrganizationService);

    statusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' }
    ];

    form = this.fb.group({
        name: ['', Validators.required],
        code: ['', Validators.required],
        status: ['ACTIVE', Validators.required]
    });

    constructor(route: ActivatedRoute, router: Router) {
        super(route, router);
    }

    ngOnInit(): void {
        this.init(); // 🔥 detects edit/create and calls load() if edit
    }

    // 🔥 Called only in EDIT mode
    load(): void {
        // 🔸 Replace with API call
        // this.service.getById(this.id!).subscribe(...)
        const mock = {
            id: this.id,
            name: 'Qatar Airways',
            code: 'QR',
            status: 'ACTIVE'
        };

        this.form.patchValue(mock);
    }

    submit(): void {
        if (this.form.invalid) return;

        const payload = this.form.value as Organization;

        if (this.isEdit) {
            // 🔸 Replace with API
            console.log('Update org', this.id, payload);
            // this.service.update(this.id!, payload).subscribe(...)
        } else {
            console.log('Create org', payload);
            // this.service.create(payload).subscribe(...)
        }

        this.navigateBack('/settings/organizations');
    }
}
