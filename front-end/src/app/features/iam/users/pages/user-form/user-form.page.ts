import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { CheckboxModule } from 'primeng/checkbox';
import { ButtonModule } from 'primeng/button';

import { UserService } from '@features/iam/users/services/user.service';
import { BaseFormComponent } from '@core/base/base-form.component';
import {SelectModule} from "primeng/select";
import {User} from "@features/iam/models/user.model";
import {ConfirmDialogModule} from "primeng/confirmdialog";

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
        ConfirmDialogModule
    ],
    templateUrl: './user-form.page.html'
})
export class UserFormPage extends BaseFormComponent<User> implements OnInit {

    private fb = inject(FormBuilder);
    private service = inject(UserService);

    ngOnInit(): void {
        this.init();
    }

    tenants = [
        { id: 'QR', name: 'Qatar Airways' },
        { id: 'EK', name: 'Emirates' }
    ];

    form = this.fb.group({
        username: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        password: [''],
        firstName: [''],
        lastName: [''],
        enabled: [true],
        accountNonLocked: [true],
        tenantId: ['', Validators.required]
    });

    constructor() {
        super(inject(ActivatedRoute), inject(Router));
    }

    load() {
        this.service.getById(this.id!).subscribe((res: any) => {
            const user = res?.body ?? res;

            this.form.patchValue({
                ...user,
                tenantId: user?.tenant?.id
            });
        });
    }

    submit() {
        if (this.form.invalid) return;

        const payload: any = this.form.value;

        if (this.id && !payload.password) {
            delete payload.password;
        }

        if (this.isEdit) {
            this.service.update(this.id!, payload).subscribe(() =>
                this.navigateBack('/iam/users')
            );
        } else {
            this.service.create(payload).subscribe(() =>
                this.navigateBack('/iam/users')
            );
        }
    }
}
