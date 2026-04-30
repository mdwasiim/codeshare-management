import { Component, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import {ConfirmationService, MessageService} from 'primeng/api';

import {forkJoin, Observable, of} from 'rxjs';

import { BaseListComponent } from '@core/base/base-list.component';
import { Organization } from "@features/settings/model/organization.model";

import { ToolbarActionComponent } from '@shared/toolbar/toolbar-action.component';
import {OrganizationFormPage} from "@features/settings/organizations/pages/organization-form/organization-form.page";

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
        ConfirmDialogModule,
        ToastModule,
        ToolbarActionComponent,
        OrganizationFormPage
    ],
    templateUrl: './organization-list.page.html',
    providers: [ConfirmationService, MessageService]
})
export class OrganizationListPage extends BaseListComponent<Organization> {

    private service = new MockOrgService(); // replace later
    private confirmationService = inject(ConfirmationService);

    dialogVisible = false;
    selectedOrganizationId: string | null = null;
    selectedOrganizations: Organization[] = [];

    @ViewChild('dt') dt!: Table;

    fetch() {
        return this.service.getAll();
    }

    // =========================
    // Toolbar Actions
    // =========================

    openCreate() {
        this.selectedOrganizationId = null;
        this.dialogVisible = true;
    }

    openEdit(org: Organization) {
        this.selectedOrganizationId = org.id ?? null;
        this.dialogVisible = true;
    }

    deleteSelectedOrganizations() {
        if (!this.selectedOrganizations.length) return;

        this.confirmationService.confirm({
            message: 'Delete selected organizations?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                const requests = this.selectedOrganizations.map(o =>
                    this.service.delete(o.id!)
                );

                forkJoin(requests).subscribe(() => {
                    this.refresh();
                    this.selectedOrganizations = [];
                });
            }
        });
    }

    deleteOrganization(org: Organization) {
        this.confirmationService.confirm({
            message: `Delete "${org.name}"?`,
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.service.delete(org.id!)
                    .subscribe(() => this.refresh());
            }
        });
    }

    onSaved() {
        this.refresh();
    }

    onSearch(value: string) {
        this.dt.filterGlobal(value, 'contains');
    }
}
