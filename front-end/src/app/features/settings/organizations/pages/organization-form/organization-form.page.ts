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

import { Organization } from "@features/settings/model/organization.model";
import { BaseCrudForm } from "@shared/components/base/base-form.component";
import { of } from "rxjs";

import { CsmDialogComponent } from '@shared/components/csm-dialog/csm-dialog.component';
import { CsmFormSectionComponent } from '@shared/components/form-section/csm-form-section.component';

@Component({
    selector: 'organization-form',
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
    templateUrl: './organization-form.page.html'
})
export class OrganizationFormPage
    extends BaseCrudForm<Organization>
    implements OnInit, OnChanges {

    @Input() visible = false;
    @Output() visibleChange = new EventEmitter<boolean>();

    private fb = inject(FormBuilder);

    statusOptions = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Inactive', value: 'INACTIVE' }
    ];

    // =========================
    // Lifecycle
    // =========================

    ngOnInit(): void {
        this.buildForm();
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
            name: ['', Validators.required],
            code: ['', Validators.required],
            status: ['ACTIVE', Validators.required]
        });
    }

    override patchForm(data: Organization): void {
        this.form.patchValue(data);
    }

    override fetchById(id: string) {
        return of({
            id,
            name: 'Qatar Airways',
            code: 'QR',
            status: 'ACTIVE' as 'ACTIVE' | 'INACTIVE'   // ✅ FIX
        });
    }

    override create(payload: any) {
        console.log('Create org', payload);
        return of(true);
    }

    override update(id: string, payload: any) {
        console.log('Update org', id, payload);
        return of(true);
    }

    // ✅ clean
    override submit(): void {
        super.submit();
    }
}
