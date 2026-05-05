import { Component, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';

import { forkJoin, Observable, of } from 'rxjs';

import { BaseListComponent } from '@shared/components/base/base-list.component';
import { Organization } from "@features/settings/model/organization.model";

import { ToolbarActionComponent } from '@shared/toolbar/toolbar-action.component';
import { OrganizationFormPage } from "@features/settings/organizations/pages/organization-form/organization-form.page";

// ✅ your wrappers
import { AppToastService } from '@core/services/app-toast.service';
import { CsmConfirmService } from '@core/services/csm-confirm.service';
import {TooltipModule} from "primeng/tooltip";

// TEMP mock
class MockOrgService {
    getAll(): Observable<Organization[]> {
        return of([
            { id: '1', name: 'Qatar Airways', code: 'QR', status: 'ACTIVE' },
            { id: '2', name: 'Emirates', code: 'EK', status: 'ACTIVE' }
        ]);
    }

    delete(id: string): Observable<boolean> {
        return of(true);
    }
}

@Component({
    selector: 'organization-list',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        TagModule,
        TooltipModule,
        ToolbarActionComponent,
        OrganizationFormPage
    ],
    templateUrl: './organization-list.page.html'
})
export class OrganizationListPage extends BaseListComponent<Organization> {

    private service = new MockOrgService(); // replace later
    private toast = inject(AppToastService);
    private confirm = inject(CsmConfirmService);

    dialogVisible = false;
    selectedOrganizationId: string | null = null;
    selectedOrganizations: Organization[] = [];

    @ViewChild('dt') dt!: Table;

    fetch() {
        return this.service.getAll();
    }

    // =========================
    // Actions
    // =========================

    openCreate() {
        this.dialogVisible = false;

        setTimeout(() => {
            this.selectedOrganizationId = null;
            this.dialogVisible = true;
        });
    }

    openEdit(org: Organization) {
        this.selectedOrganizationId = org.id ?? null;
        this.dialogVisible = true;
    }

    deleteSelectedOrganizations() {
        if (!this.selectedOrganizations.length) return;

        this.confirm.delete('Delete selected organizations?', () => {
            const requests = this.selectedOrganizations.map(o =>
                this.service.delete(o.id!)
            );

            forkJoin(requests).subscribe({
                next: () => {
                    this.toast.success('Organizations deleted successfully');
                    this.refresh();
                    this.selectedOrganizations = [];
                },
                error: () => {
                    this.toast.error('Failed to delete organizations');
                }
            });
        });
    }

    deleteOrganization(org: Organization) {
        this.confirm.delete(
            `Delete "${org.name}"?`,
            () => {
                this.service.delete(org.id!).subscribe({
                    next: () => {
                        this.toast.success('Organization deleted successfully');
                        this.refresh();
                    },
                    error: () => {
                        this.toast.error('Delete failed');
                    }
                });
            }
        );
    }

    onSaved() {
        this.toast.success('Organization saved successfully');
        this.dialogVisible = false;
        this.refresh();
    }

    onSearch(value: string) {
        this.dt.filterGlobal(value, 'contains');
    }

    exportCSV() {
        this.dt.exportCSV();
    }
}
