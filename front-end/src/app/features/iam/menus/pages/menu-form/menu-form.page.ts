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

import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';

import { AppMenuModel } from "@shared/models/app-menu.model";
import { BaseCrudForm } from "@shared/components/base/base-form.component";
import { LayoutMenuService } from "@layout/services/layout-menu.service";
import { MenuManagementService } from "@features/iam/menus/services/menu-management.service";
import {SelectModule} from "primeng/select";
import {CsmDialogComponent} from "@shared/components/csm-dialog/csm-dialog.component";
import {CsmFormSectionComponent} from "@shared/components/form-section/csm-form-section.component";

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
        CsmDialogComponent,
        CsmFormSectionComponent
    ],
    templateUrl: './menu-form.page.html'
})
export class MenuFormPage extends BaseCrudForm<AppMenuModel>
    implements OnInit, OnChanges {

    @Input() visible = false;
    @Output() visibleChange = new EventEmitter<boolean>();

    menuOptions: any[] = [];

    private fb = inject(FormBuilder);
    private service = inject(MenuManagementService);
    private layoutMenu = inject(LayoutMenuService);

    ngOnInit(): void {
        this.buildForm();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['visible'] && this.visible) {
            this.init();
            this.loadMenuOptions();
        }
    }

    // =========================
    // FORM
    // =========================

    override buildForm(): void {
        this.form = this.fb.group({
            id: [null],
            label: ['', Validators.required],
            icon: [''],
            routerLink: [''],
            displayOrder: [0],
            parentId: [null]
        });
    }

    override patchForm(data: AppMenuModel): void {
        this.form.patchValue({
            ...data,
            routerLink: data.routerLink?.[0] || ''
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
                    value: m.id
                }));

                // prevent self-parent
                if (this.id) {
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
            displayOrder: formValue.displayOrder ?? 0,
            parentId: formValue.parentId ?? undefined,
            routerLink: formValue.routerLink?.trim()
                ? [formValue.routerLink.trim()]
                : undefined,
            route: formValue.routerLink?.trim() || undefined
        };
    }

    // =========================
    // AFTER SAVE
    // =========================

    override submit(): void {
        super.submit();
    }
}
