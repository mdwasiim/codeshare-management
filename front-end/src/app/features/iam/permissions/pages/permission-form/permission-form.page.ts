import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';

import { BaseFormComponent } from '@core/base/base-form.component';
import { Permission } from '@features/iam/models/permission.model';
import { PermissionService } from '@features/iam/permissions/services/permission.service';

@Component({
    selector: 'permission-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule
    ],
    templateUrl: './permission-form.page.html'
})
export class PermissionFormPage extends BaseFormComponent<Permission> {

    private fb = inject(FormBuilder);
    private service = inject(PermissionService);

    form = this.fb.group({
        name: ['', Validators.required],
        domain: ['', Validators.required],
        action: ['', Validators.required],
        description: ['']
    });

    constructor() {
        super(inject(ActivatedRoute), inject(Router));
    }

    load() {
        this.service.getById(this.id!).subscribe((res: any) => {
            const data = res?.body ?? res;
            this.form.patchValue(data);
        });
    }

    submit() {
        if (this.form.invalid) return;

        const payload = this.form.value as Permission;

        if (this.isEdit) {
            this.service.update(this.id!, payload).subscribe(() =>
                this.navigateBack('/iam/permissions')
            );
        } else {
            this.service.create(payload).subscribe(() =>
                this.navigateBack('/iam/permissions')
            );
        }
    }
}
