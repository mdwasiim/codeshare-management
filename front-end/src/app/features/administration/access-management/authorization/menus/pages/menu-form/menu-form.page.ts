import { Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';

import { AppMenuModel } from '@features/administration/access-management/models/app-menu.model';
import { BaseCrudForm } from '@shared/components/base/base-form.component';
import { MenuManagementService } from '@features/administration/access-management/authorization/menus/services/menu-management.service';
import { SelectModule } from 'primeng/select';
import { AppFormSectionComponent } from '@shared/components/form-section/app-form-section.component';
import { GroupService } from '@features/administration/access-management/identity/groups/services/group.service';
import { MultiSelectModule } from 'primeng/multiselect';

@Component({
    selector: 'menu-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, InputTextModule, ButtonModule, DialogModule, SelectModule, MultiSelectModule, AppFormSectionComponent],
    templateUrl: './menu-form.page.html'
})
export class MenuFormPage extends BaseCrudForm<AppMenuModel> {
    @Input() visible = false;
    @Output() visibleChange = new EventEmitter<boolean>();

    menuOptions: { label: string; value: string }[] = [];

    private fb = inject(FormBuilder);
    private service = inject(MenuManagementService);
    private groupService = inject(GroupService);

    groups: { label: string; value: string }[] = [];
    navigationTypes = [
        { label: 'Section', value: 'SECTION' },
        { label: 'Internal Link', value: 'INTERNAL_LINK' },
        { label: 'External Link', value: 'EXTERNAL_LINK' }
    ];

    override ngOnInit(): void {
        super.ngOnInit();

        this.loadGroups();
        this.loadMenuOptions();
    }

    loadGroups() {
        this.groupService.getAll().subscribe({
            next: (res) => {
                this.groups = res.map((g) => ({
                    label: g.name,
                    value: g.id!
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
            code: [null],
            label: ['', Validators.required],
            topbarLabel: [''],
            sidebarLabel: [''],
            icon: [''],
            navigationType: ['SECTION', Validators.required],
            frontendPath: [''],
            externalUrl: [''],
            permissionCode: [''],
            displayOrder: [0],
            parentId: [null],
            groupIds: [[]]
        });

        this.form.get('navigationType')?.valueChanges.subscribe((value) => {
            if (value !== 'INTERNAL_LINK') {
                this.form.get('frontendPath')?.setValue('', { emitEvent: false });
            }
            if (value !== 'EXTERNAL_LINK') {
                this.form.get('externalUrl')?.setValue('', { emitEvent: false });
            }
        });
    }

    get isRootMenu(): boolean {
        return !this.form?.get('parentId')?.value;
    }

    get isInternalLinkMenu(): boolean {
        return this.form?.get('navigationType')?.value === 'INTERNAL_LINK';
    }

    get isExternalLinkMenu(): boolean {
        return this.form?.get('navigationType')?.value === 'EXTERNAL_LINK';
    }

    override patchForm(data: AppMenuModel): void {
        this.form.patchValue({
            ...data,
            navigationType: data.navigationType || (data.externalUrl ? 'EXTERNAL_LINK' : data.frontendPath ? 'INTERNAL_LINK' : 'SECTION'),
            frontendPath: data.frontendPath || '',
            externalUrl: data.externalUrl || '',
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
            next: (res) => {
                this.menuOptions = res.map((m) => ({
                    label: m.label,
                    value: m.id!
                }));
                // prevent self-parent
                const currentId = this.form.get('id')?.value;
                if (currentId) {
                    this.menuOptions = this.menuOptions.filter((m) => m.value !== this.id);
                }
            }
        });
    }

    // =========================
    // MAPPER
    // =========================

    private mapToModel(formValue: any): AppMenuModel {
        const isRootMenu = !formValue.parentId;

        return {
            id: formValue.id ?? undefined,
            code: formValue.code,
            label: formValue.label,
            topbarLabel: isRootMenu ? formValue.topbarLabel?.trim() || undefined : undefined,
            sidebarLabel: !isRootMenu ? formValue.sidebarLabel?.trim() || undefined : undefined,
            icon: formValue.icon || undefined,
            navigationType: formValue.navigationType || 'SECTION',
            frontendPath: formValue.navigationType === 'INTERNAL_LINK' ? formValue.frontendPath?.trim() || undefined : undefined,
            externalUrl: formValue.navigationType === 'EXTERNAL_LINK' ? formValue.externalUrl?.trim() || undefined : undefined,
            permissionCode: formValue.permissionCode?.trim() || undefined,
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

