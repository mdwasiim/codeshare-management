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
import { BaseCrudForm } from "@core/base/base-crud-form.component";
import { LayoutMenuService } from "@layout/services/layout-menu.service";
import { MenuManagementService } from "@features/iam/menus/services/menu-management.service";

@Component({
    selector: 'menu-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule,
        DialogModule
    ],
    templateUrl: './menu-form.page.html'
})
export class MenuFormPage extends BaseCrudForm<AppMenuModel>
    implements OnInit, OnChanges {

    @Input() visible = false;

    @Output() visibleChange = new EventEmitter<boolean>();

    private fb = inject(FormBuilder);
    private service = inject(MenuManagementService);
    private layoutMenu = inject(LayoutMenuService);


    constructor() {
        super();
    }

    // =========================
    // INIT
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
    // BASE METHODS
    // =========================

    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            label: ['', Validators.required],
            icon: [''],
            routerLink: [''],
            displayOrder: [0],
            parentId: [null as string | null]
        });
    }

    patchForm(data: AppMenuModel): void {
        this.form.patchValue({
            ...data,
            routerLink: data.routerLink?.[0] || ''
        });
    }

    fetchById(id: string) {
        return this.service.getById(id);
    }

    create(payload: any) {
        debugger;
        return this.service.create(this.mapToModel(payload));
    }

    update(id: string, payload: any) {
        return this.service.update(id, this.mapToModel(payload));
    }

    // =========================
    // MAPPER
    // =========================
    private mapToModel(formValue: any): AppMenuModel {
        return {
            id: formValue.id ?? undefined,
            label: formValue.label!,
            icon: formValue.icon || undefined,
            displayOrder: formValue.displayOrder ?? 0,
            parentId: formValue.parentId ?? undefined,
            routerLink: formValue.routerLink?.trim()
                ? [formValue.routerLink.trim()]
                : undefined
        };
    }

    // =========================
    // DELETE
    // =========================
    delete(): void {
        if (!this.id) return;
        if (!confirm('Delete this menu?')) return;

        this.service.delete(this.id).subscribe(() => {
            this.layoutMenu.loadMenus().subscribe();
            this.saved.emit();
            this.visibleChange.emit(false);
        });
    }

    // =========================
    // AFTER SAVE HOOK
    // =========================
    override submit(): void {
        super.submit();

        // handle after save via event
        this.saved.subscribe(() => {
            this.layoutMenu.loadMenus().subscribe();
            this.visibleChange.emit(false);
        });
    }
}
