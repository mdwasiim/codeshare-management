import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AppMenuModel } from "@shared/models/app-menu.model";
import { MenuManagementService } from "@features/iam/menu-management/services/menu-management.service";
import { BaseFormComponent } from "@core/base/base-form.component";
import { ActivatedRoute, Router } from "@angular/router";
import { LayoutMenuService } from "@layout/services/layout-menu.service";

@Component({
    selector: 'menu-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule
    ],
    templateUrl: './menu-form.page.html'
})
export class MenuFormPage extends BaseFormComponent<AppMenuModel> implements OnInit {

    private fb = inject(FormBuilder);
    private service = inject(MenuManagementService);
    private layoutMenu = inject(LayoutMenuService);

    loading = false;

    constructor() {
        super(inject(ActivatedRoute), inject(Router));
    }

    ngOnInit(): void {
        this.init();
    }

    form = this.fb.group({
        id: [null as string | null],
        label: ['', Validators.required],
        icon: [''],
        routerLink: [''],
        displayOrder: [0],
        parentId: [null as string | null]
    });

    // =========================
    // LOAD (EDIT MODE)
    // =========================
    load() {
        if (!this.id) return;

        this.service.getById(this.id).subscribe((res: any) => {
            const user = res?.body ?? res;

            this.form.patchValue({
                ...user,
                tenantId: user?.tenant?.id
            });
        });
    }

    // =========================
    // SAVE
    // =========================
    submit() {
        if (this.form.invalid) return;

        this.loading = true;

        const formValue = this.form.value;

        const payload: AppMenuModel = {
            id: formValue.id ?? undefined,
            label: formValue.label!,   // required
            icon: formValue.icon || undefined,
            displayOrder: formValue.displayOrder ?? 0,
            parentId: formValue.parentId ?? undefined,

            // ✅ ALWAYS normalize to array
            routerLink: formValue.routerLink?.trim()
                ? [formValue.routerLink.trim()]
                : undefined
        };

        (this.isEdit
                ? this.service.update(this.id!, payload)
                : this.service.create(payload)
        ).subscribe({
            next: () => {
                this.loading = false;

                // 🔥 refresh sidebar
                this.layoutMenu.loadMenus().subscribe();

                this.navigateBack('/iam/menu');
            },
            error: (err) => {
                console.error(err);
                this.loading = false;
            }
        });
    }

    // =========================
    // DELETE
    // =========================
    delete() {
        if (!this.id) return;
        if (!confirm('Delete this menu?')) return;

        this.service.delete(this.id).subscribe(() => {
            this.layoutMenu.loadMenus().subscribe();
            this.navigateBack('/iam/menu');
        });
    }
}
