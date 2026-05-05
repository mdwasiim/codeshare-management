import {Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';

import {CommonModule} from '@angular/common';
import {FormBuilder, ReactiveFormsModule} from '@angular/forms';

import {InputTextModule} from 'primeng/inputtext';
import {ButtonModule} from 'primeng/button';
import {SelectModule} from 'primeng/select';

import {Permission} from '@features/iam/models/permission.model';
import {PermissionApiService} from '@features/iam/permissions/services/permission-api.service';
import {TenantService} from '@features/iam/tenants/services/tenant.service';

import {BaseCrudForm} from '@shared/components/base/base-form.component';
import {CsmDialogComponent} from "@shared/components/csm-dialog/csm-dialog.component";
import {CsmFormSectionComponent} from "@shared/components/form-section/csm-form-section.component";

@Component({
    selector: 'permission-form',
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
    templateUrl: './permission-form.page.html'
})
export class PermissionFormPage
    extends BaseCrudForm<Permission>
    implements OnInit, OnChanges {

    @Input() visible = false;
    @Output() visibleChange = new EventEmitter<boolean>();

    private fb = inject(FormBuilder);
    private service = inject(PermissionApiService);
    private tenantService = inject(TenantService);

    tenants: any[] = [];
    domains: any[] = [];
    actions: any[] = [];

    ngOnInit(): void {
        this.buildForm();

        this.tenantService.getAll().subscribe({
            next: res => this.tenants = res
        });

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

    override buildForm(): void {
        this.form = this.fb.group({
            id: [null],
            name: [''],
            domain: [''],
            action: [''],
            description: [''],
            tenantId: ['']
        });
    }

    override patchForm(data: Permission): void {
        this.form.patchValue(data);
    }

    override fetchById(id: string) {
        return this.service.getById(id);
    }

    override create(payload: any) {
        payload.domain = payload.domain?.toUpperCase();
        payload.action = payload.action?.toUpperCase();
        return this.service.create(payload);
    }

    override update(id: string, payload: any) {
        payload.domain = payload.domain?.toUpperCase();
        payload.action = payload.action?.toUpperCase();
        return this.service.update(id, payload);
    }
}
