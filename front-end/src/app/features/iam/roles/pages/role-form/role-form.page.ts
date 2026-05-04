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
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';

import { Role } from '@features/iam/models/role.model';
import { RoleService } from '../../services/role.service';
import { BaseCrudForm } from '@core/base/base-crud-form.component';

import { TenantService } from '@features/iam/tenants/services/tenant.service';
import { Tenant } from '@features/iam/models/tenant.model';

import { CsmDialogComponent } from '@shared/components/csm-dialog/csm-dialog.component';
import { CsmFormSectionComponent } from '@shared/components/form-section/csm-form-section.component';

@Component({
    selector: 'role-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule,
        SelectModule,
        CsmDialogComponent,
        CsmFormSectionComponent
    ],
    templateUrl: './role-form.page.html'
})
export class RoleFormPage
    extends BaseCrudForm<Role>
    implements OnInit, OnChanges {

    @Input() visible = false;
    @Output() visibleChange = new EventEmitter<boolean>();

    tenants: Tenant[] = [];

    private fb = inject(FormBuilder);
    private service = inject(RoleService);
    private tenantService = inject(TenantService);

    // =========================
    // Lifecycle
    // =========================

    ngOnInit(): void {
        this.buildForm();

        this.tenantService.getAll().subscribe({
            next: res => this.tenants = res
        });
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['visible'] && this.visible) {
            this.init();
        }
    }

    // =========================
    // Form
    // =========================

    override buildForm(): void {
        this.form = this.fb.group({
            id: [null],
            code: ['', Validators.required],
            name: ['', Validators.required],
            description: [''],
            tenantId: [null, Validators.required]
        });
    }

    override patchForm(data: Role): void {
        this.form.patchValue(data);
    }

    override fetchById(id: string) {
        return this.service.getById(id);
    }

    override create(payload: any) {
        return this.service.create(payload);
    }

    override update(id: string, payload: any) {
        return this.service.update(id, payload);
    }

    // ✅ CLEAN (no subscribe here)
    override submit(): void {
        super.submit();
    }
}
