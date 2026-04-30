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
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';

import { Role } from '@features/iam/models/role.model';
import { RoleService } from '../../services/role.service';
import { BaseCrudForm } from '@core/base/base-crud-form.component';

@Component({
    selector: 'role-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule,
        DialogModule
    ],
    templateUrl: './role-form.page.html'
})
export class RoleFormPage
    extends BaseCrudForm<Role>
    implements OnInit, OnChanges {

    // =========================
    // Dialog Inputs
    // =========================
    @Input() visible = false;

    @Output() visibleChange = new EventEmitter<boolean>();

    private fb = inject(FormBuilder);
    private service = inject(RoleService);


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
            code: ['', Validators.required],
            name: ['', Validators.required],
            description: [''],
            tenantId: ['QR', Validators.required]
        });
    }

    patchForm(data: Role): void {
        this.form.patchValue(data);
    }

    fetchById(id: string) {
        return this.service.getById(id);
    }

    create(payload: any) {
        return this.service.create(payload);
    }

    update(id: string, payload: any) {
        return this.service.update(id, payload);
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
