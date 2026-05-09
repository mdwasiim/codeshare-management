import {Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';

import {CommonModule} from '@angular/common';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';

import {ButtonModule} from 'primeng/button';
import {InputTextModule} from 'primeng/inputtext';
import {DialogModule} from 'primeng/dialog';

import {AppMenuModel} from "@features/iam/models/app-menu.model";
import {BaseCrudForm} from "@shared/components/base/base-form.component";
import {MenuManagementService} from "@features/iam/menus/services/menu-management.service";
import {SelectModule} from "primeng/select";
import {CsmFormSectionComponent} from "@shared/components/form-section/csm-form-section.component";
import {GroupService} from "@features/iam/groups/services/group.service";
import {MultiSelectModule} from "primeng/multiselect";
import {PermissionApiService} from "@features/iam/permissions/services/permission-api.service";
import {Permission} from "@features/iam/models/permission.model";

@Component({
    selector: 'menu-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule,
        DialogModule,
        SelectModule,
        MultiSelectModule,
        CsmFormSectionComponent
    ],
    templateUrl: './menu-form.page.html'
})
export class MenuFormPage extends BaseCrudForm<AppMenuModel> {

    @Input() visible = false;
    @Output() visibleChange = new EventEmitter<boolean>();

    menuOptions: { label: string; value: string }[] = [];

    private fb = inject(FormBuilder);
    private service = inject(MenuManagementService);
    private groupService = inject(GroupService);
    private permissionApiService = inject(PermissionApiService);

    override ngOnInit(): void {

        super.ngOnInit();

        this.loadGroups();
        this.loadMenuOptions();
        this.loadPermissions();
    }

    groups: { label: string; value: string }[] = [];
    permissionOptions: { label: string; value: string }[] = [];

    loadGroups() {
        this.groupService.getAll().subscribe({
            next: res => {
                this.groups = res.map(g => ({
                    label: g.name,
                    value: g.id!
                }));
            }
        });
    }

    loadPermissions() {
        this.permissionApiService.getAll().subscribe({
            next: (res: Permission[]) => {
                this.permissionOptions = res.map(p => ({
                    label: p.code!,
                    value: p.code!
                }));
            }
        });
    }

    // =========================
    // FORM
    // =========================

    override buildForm(): void {
        this.form = this.fb.group({
            id: [null],
            label: ['', Validators.required],
            icon: [''],
            route: [''],
            permission: [''],
            displayOrder: [0],
            parentId: [null],
            groupIds: [[]]
        });
    }

    override patchForm(data: AppMenuModel): void {
        this.form.patchValue({
            ...data,
            route: data.route || '',
            groupIds: data.groupIds || []
        });
    }

    override fetchById(id: string) {
        return this.service.getById(id);
    }

    override create(payload: any) {
        return this.service.create(this.mapToModel(payload));
    }

    override update(id: string, payload: any) {
        return this.service.update(id, this.mapToModel(payload));
    }

    // =========================
    // LOAD OPTIONS
    // =========================

    loadMenuOptions() {
        this.service.getAll().subscribe({
            next: res => {
                this.menuOptions = res.map(m => ({
                    label: m.label,
                    value: m.id!
                }));
                // prevent self-parent
                const currentId = this.form.get('id')?.value;
                if (currentId) {
                    this.menuOptions = this.menuOptions.filter(m => m.value !== this.id);
                }
            }
        });
    }

    // =========================
    // MAPPER
    // =========================

    private mapToModel(formValue: any): AppMenuModel {
        return {
            id: formValue.id ?? undefined,
            label: formValue.label,
            icon: formValue.icon || undefined,
            route: formValue.route?.trim() || undefined,
            permission: formValue.permission?.trim() || undefined,
            displayOrder: formValue.displayOrder ?? 0,
            parentId: formValue.parentId ?? undefined,
            groupIds: formValue.groupIds || []
        };
    }

    // =========================
    // AFTER SAVE
    // =========================

    override submit(): void {
        super.submit();
    }
}
