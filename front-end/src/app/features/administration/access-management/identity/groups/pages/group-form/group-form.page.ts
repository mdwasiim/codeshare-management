import { Component, EventEmitter, Input, Output, inject } from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';

import { Group } from '@features/administration/access-management/models/group.model';
import { GroupService } from '../../services/group.service';
import { BaseCrudForm } from '@shared/components/base/base-form.component';
import { Tenant } from '@features/administration/tenant-management/models/tenant.model';
import { TenantService } from '@features/administration/tenant-management/tenant-onboarding/tenant-administration/tenants/services/tenant.service';
import { AppFormSectionComponent } from '@shared/components/form-section/app-form-section.component';

@Component({
    selector: 'group-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, InputTextModule, ButtonModule, SelectModule, AppFormSectionComponent],
    templateUrl: './group-form.page.html'
})
export class GroupFormPage extends BaseCrudForm<Group> {
    @Input() visible = false;
    @Output() visibleChange = new EventEmitter<boolean>();

    tenants: Tenant[] = [];

    private readonly fb = inject(FormBuilder);
    private readonly service = inject(GroupService);
    private readonly tenantService = inject(TenantService);

    override ngOnInit(): void {
        super.ngOnInit();
        this.tenantService.getAll().subscribe({
            next: (res: Tenant[]) => (this.tenants = res)
        });
    }

    override buildForm(): void {
        this.form = this.fb.group({
            id: [null],
            code: ['', Validators.required],
            name: ['', Validators.required],
            description: [''],
            tenantId: [null, Validators.required]
        });
    }

    override patchForm(data: Group): void {
        this.form.patchValue(data);
    }

    override fetchById(id: string) {
        return this.service.getById(id);
    }

    override create(payload: Group) {
        return this.service.create(payload);
    }

    override update(id: string, payload: Group) {
        return this.service.update(id, payload);
    }
}
