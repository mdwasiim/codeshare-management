import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { GroupService } from '../../services/group.service';
import { BaseFormComponent } from '@core/base/base-form.component';
import { Group } from '@features/iam/models/group.model';

@Component({
    selector: 'app-group-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './group-form.page.html'
})
export class GroupFormPage extends BaseFormComponent<Group> {

    private fb = inject(FormBuilder);
    private service = inject(GroupService);

    form = this.fb.group({
        code: ['', Validators.required],
        name: ['', Validators.required],
        description: [''],
        tenantId: ['QR', Validators.required] // temp
    });

    constructor() {
        super(inject(ActivatedRoute), inject(Router));
    }

    load() {
        this.service.getById(this.id!).subscribe((res: any) => {
            const data = res?.body ?? res;
            this.form.patchValue(data);
        });
    }

    submit() {
        if (this.form.invalid) return;

        const payload = this.form.value as Group;

        if (this.isEdit) {
            this.service.update(this.id!, payload).subscribe(() =>
                this.navigateBack('/iam/groups')
            );
        } else {
            this.service.create(payload).subscribe(() =>
                this.navigateBack('/iam/groups')
            );
        }
    }
}
