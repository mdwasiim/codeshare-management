import { Component, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';

import { forkJoin } from 'rxjs';

import { GroupService } from '../../services/group.service';
import { Group } from '@features/iam/models/group.model';
import { BaseListComponent } from '@core/base/base-list.component';

import { ToolbarActionComponent } from '@shared/toolbar/toolbar-action.component';
import { GroupFormPage } from '@features/iam/groups/pages/group-form/group-form.page';

// ✅ wrapper services
import { AppToastService } from '@core/services/app-toast.service';
import { CsmConfirmService } from '@core/services/csm-confirm.service';
import {Tooltip} from "primeng/tooltip";

@Component({
    selector: 'app-group-list',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        TagModule,
        ToolbarActionComponent,
        GroupFormPage,
        Tooltip
    ],
    templateUrl: './group-list.page.html'
})
export class GroupListPage extends BaseListComponent<Group> {

    private service = inject(GroupService);
    private toast = inject(AppToastService);
    private confirm = inject(CsmConfirmService);

    dialogVisible = false;
    selectedGroupId: string | null = null;
    selectedGroups: Group[] = [];

    @ViewChild('dt') dt!: Table;

    fetch() {
        return this.service.getAll();
    }

    // =========================
    // Actions
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

        this.confirm.delete('Delete selected groups?', () => {
            const requests = this.selectedGroups.map(g =>
                this.service.delete(g.id!)
            );

            forkJoin(requests).subscribe({
                next: () => {
                    this.toast.success('Groups deleted successfully');
                    this.refresh();
                    this.selectedGroups = [];
                },
                error: () => {
                    this.toast.error('Failed to delete groups');
                }
            });
        });
    }

    deleteGroup(group: Group) {
        this.confirm.delete(
            `Delete group "${group.name}"?`,
            () => {
                this.service.delete(group.id!).subscribe({
                    next: () => {
                        this.toast.success('Group deleted successfully');
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
        this.toast.success('Group saved successfully');
        this.dialogVisible = false;
        this.refresh();
    }

    onSearch(value: string) {
        this.dt.filterGlobal(value, 'contains');
    }
}
