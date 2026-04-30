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

import { Group } from '@features/iam/models/group.model';
import { GroupService } from '../../services/group.service';
import { BaseCrudForm } from '@core/base/base-crud-form.component';

@Component({
    selector: 'group-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule,
        DialogModule
    ],
    templateUrl: './group-form.page.html'
})
export class GroupFormPage
    extends BaseCrudForm<Group>
    implements OnInit, OnChanges {

    // =========================
    // Dialog Inputs
    // =========================
    @Input() visible = false;

    @Output() visibleChange = new EventEmitter<boolean>();

    private fb = inject(FormBuilder);
    private service = inject(GroupService);


    // =========================
    // Lifecycle
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
    // BaseCrudForm Methods
    // =========================

    buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            code: ['', Validators.required],
            name: ['', Validators.required],
            description: [''],
            tenantId: ['QR', Validators.required]
        });
    }

    patchForm(data: Group): void {
        this.form.patchValue(data);
    }

    fetchById(id: string) {
        return this.service.getById(id);
    }

    create(payload: any) {
        return this.service.create(payload);
    }

    update(id: string, payload: any) {
        return this.service.update(id, payload);
    }

    // =========================
    // After Save Hook
    // =========================
    override submit(): void {
        super.submit();

        this.saved.subscribe(() => {
            this.visibleChange.emit(false);
        });
    }
}
