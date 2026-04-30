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
import { SelectModule } from 'primeng/select';

import { Organization } from "@features/settings/model/organization.model";
import { BaseCrudForm } from "@core/base/base-crud-form.component";
import {of} from "rxjs";

@Component({
    selector: 'organization-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule,
        DialogModule,
        SelectModule
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

    ngOnInit(): void {
        this.buildForm();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['visible'] && this.visible) {
            this.init(); // 🔥 important
        }
    }

    // =========================
    // BaseCrudForm methods
    // =========================

    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            name: ['', Validators.required],
            code: ['', Validators.required],
            status: ['ACTIVE', Validators.required]
        });
    }

    patchForm(data: Organization): void {
        this.form.patchValue(data);
    }

    fetchById(id: string) {
        // 🔥 replace with API later
        return of({
            id,
            name: 'Qatar Airways',
            code: 'QR',
            status: 'ACTIVE'
        });
    }

    create(payload: any) {
        console.log('Create org', payload);
        return of(true);
    }

    update(id: string, payload: any) {
        console.log('Update org', id, payload);
        return of(true);
    }

    override submit(): void {
        super.submit();

        this.saved.subscribe(() => {
            this.visibleChange.emit(false);
        });
    }
}
