import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';

import { BaseFormComponent } from '@core/base/base-form.component';
import { Role } from '@features/iam/models/role.model';
import { RoleService } from '../../services/role.service';

@Component({
    selector: 'role-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule
    ],
    templateUrl: './role-form.page.html'
})
export class RoleFormPage extends BaseFormComponent<Role> {

    private fb = inject(FormBuilder);
    private service = inject(RoleService);

    form = this.fb.group({
        code: ['', Validators.required],
        name: ['', Validators.required],
        description: [''],
        tenantId: ['QR', Validators.required]
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

        const payload = this.form.value as Role;

        if (this.isEdit) {
            this.service.update(this.id!, payload).subscribe(() =>
                this.navigateBack('/iam/roles')
            );
        } else {
            this.service.create(payload).subscribe(() =>
                this.navigateBack('/iam/roles')
            );
        }
    }
}
