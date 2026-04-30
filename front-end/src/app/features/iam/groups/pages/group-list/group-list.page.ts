import { Component, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import {ConfirmationService, MessageService} from 'primeng/api';

import { forkJoin } from 'rxjs';

import { GroupService } from '../../services/group.service';
import { Group } from '@features/iam/models/group.model';
import { BaseListComponent } from '@core/base/base-list.component';

import { ToolbarActionComponent } from '@shared/toolbar/toolbar-action.component';
import { GroupFormPage } from '@features/iam/groups/pages/group-form/group-form.page';

@Component({
    selector: 'app-group-list',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        TagModule,
        ConfirmDialogModule,
        ToastModule,
        ToolbarActionComponent,
        GroupFormPage
    ],
    templateUrl: './group-list.page.html',
    providers: [ConfirmationService, MessageService]
})
export class GroupListPage extends BaseListComponent<Group> {

    private service = inject(GroupService);
    private confirmationService = inject(ConfirmationService);

    tenantId = 'QR';

    dialogVisible = false;
    selectedGroupId: string | null = null;
    selectedGroups: Group[] = [];

    @ViewChild('dt') dt!: Table;

    fetch() {
        return this.service.getAll(this.tenantId);
    }

    // =========================
    // Toolbar Actions
    // =========================

    openCreate() {
        this.selectedGroupId = null;
        this.dialogVisible = true;
    }

    openEdit(group: Group) {
        this.selectedGroupId = group.id ?? null;
        this.dialogVisible = true;
    }

    deleteSelectedGroups() {
        if (!this.selectedGroups.length) return;

        this.confirmationService.confirm({
            message: 'Delete selected groups?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                const requests = this.selectedGroups.map(g =>
                    this.service.delete(g.id!)
                );

                forkJoin(requests).subscribe(() => {
                    this.refresh();
                    this.selectedGroups = [];
                });
            }
        });
    }

    deleteGroup(group: Group) {
        this.confirmationService.confirm({
            message: `Delete group "${group.name}"?`,
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.service.delete(group.id!)
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
