import {
    Component,
    EventEmitter,
    inject,
    Input,
    OnInit,
    OnChanges,
    Output,
    SimpleChanges
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';

import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { CheckboxModule } from 'primeng/checkbox';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { SelectModule } from 'primeng/select';

import { UserService } from '@features/iam/users/services/user.service';
import { User } from '@features/iam/models/user.model';
import { BaseCrudForm } from '@core/base/base-crud-form.component';

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
        DialogModule
    ],
    templateUrl: './user-form.page.html'
})
export class UserFormPage
    extends BaseCrudForm<User>
    implements OnInit, OnChanges {

    // =========================
    // Dialog Inputs
    // =========================
    @Input() visible = false;

    @Output() visibleChange = new EventEmitter<boolean>();

    private fb = inject(FormBuilder);
    private service = inject(UserService);

    // =========================
    // Static Data (demo)
    // =========================
    tenants = [
        { id: 'QR', name: 'Qatar Airways' },
        { id: 'EK', name: 'Emirates' }
    ];

    // =========================
    // Lifecycle
    // =========================
    ngOnInit(): void {
        this.buildForm();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['visible'] && this.visible) {
            this.init(); // 🔥 important
        }
    }

    // =========================
    // BaseCrudForm Methods
    // =========================

    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            username: ['', Validators.required],
            email: ['', [Validators.required, Validators.email]],
            password: [''],
            firstName: [''],
            lastName: [''],
            enabled: [true],
            accountNonLocked: [true],
            tenantId: ['', Validators.required]
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

    create(payload: any) {
        return this.service.create(this.mapToModel(payload));
    }

    update(id: string, payload: any) {
        return this.service.update(id, this.mapToModel(payload));
    }

    // =========================
    // Payload Mapper
    // =========================
    private mapToModel(formValue: any): any {
        const payload: any = { ...formValue };

        // remove password if empty in edit
        if (this.id && !payload.password) {
            delete payload.password;
        }

        return payload;
    }

    // =========================
    // After Save Hook
    // =========================
    override submit(): void {
        super.submit();

        this.saved.subscribe(() => {
            this.visibleChange.emit(false);
        });
    }
}
