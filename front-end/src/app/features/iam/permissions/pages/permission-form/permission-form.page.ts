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
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { SelectModule } from 'primeng/select';

import { Permission } from '@features/iam/models/permission.model';
import { PermissionService } from '@features/iam/permissions/services/permission.service';
import { TenantService } from '@features/iam/tenants/services/tenant.service';

import { BaseCrudForm } from '@core/base/base-crud-form.component';

@Component({
    selector: 'permission-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule,
        DialogModule,
        SelectModule // ✅ REQUIRED
    ],
    templateUrl: './permission-form.page.html'
})
export class PermissionFormPage
    extends BaseCrudForm<Permission>
    implements OnInit, OnChanges {

    @Input() visible = false;
    @Output() visibleChange = new EventEmitter<boolean>();

    private fb = inject(FormBuilder);
    private service = inject(PermissionService);
    private tenantService = inject(TenantService);

    tenants: any[] = [];
    domains: any[] = [];
    actions: any[] = [];

    ngOnInit(): void {
        this.buildForm();

        // Load tenants
        this.tenantService.getAll().subscribe(res => this.tenants = res);

        // Static domains (can be dynamic later)
        this.domains = [
            { name: 'User', code: 'USER' },
            { name: 'Group', code: 'GROUP' },
            { name: 'Permission', code: 'PERMISSION' }
        ];

        this.actions = [
            { name: 'Create', code: 'CREATE' },
            { name: 'Read', code: 'READ' },
            { name: 'Update', code: 'UPDATE' },
            { name: 'Delete', code: 'DELETE' }
        ];

        // Auto-generate name (UI-friendly)
        this.form.valueChanges.subscribe(val => {
            if (val.domain && val.action) {
                this.form.patchValue({
                    name: `${val.domain} ${val.action}`
                }, { emitEvent: false });
            }
        });
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['visible'] && this.visible) {
            this.init();
        }
    }

    buildForm(): void {
        this.form = this.fb.group({
            id: [null],
            name: [''],
            domain: [''],
            action: [''],
            description: [''],
            tenantId: ['']
        });
    }

    patchForm(data: Permission): void {
        this.form.patchValue(data);
    }

    fetchById(id: string) {
        return this.service.getById(id);
    }

    create(payload: any) {
        payload.domain = payload.domain?.toUpperCase();
        payload.action = payload.action?.toUpperCase();
        return this.service.create(payload);
    }

    update(id: string, payload: any) {
        payload.domain = payload.domain?.toUpperCase();
        payload.action = payload.action?.toUpperCase();
        return this.service.update(id, payload);
    }

    override submit(): void {
        super.submit();

        this.saved.subscribe(() => {
            this.visibleChange.emit(false);
        });
    }
}
