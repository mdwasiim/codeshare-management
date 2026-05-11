import {Component, inject, ViewChild} from '@angular/core';
import {CommonModule} from '@angular/common';

import {Table, TableModule} from 'primeng/table';
import {ButtonModule} from 'primeng/button';
import {TagModule} from 'primeng/tag';

import {forkJoin} from 'rxjs';

import {GroupService} from '../../services/group.service';
import {Group} from '@features/access-management/iam/models/group.model';
import {BaseListComponent} from '@shared/components/base/base-list.component';

import {ToolbarActionComponent} from '@shared/components/toolbar/toolbar-action.component';

// ✅ wrapper services
import {AppToastService} from '@services/app-toast.service';
import {CsmConfirmService} from '@services/csm-confirm.service';
import {Tooltip} from "primeng/tooltip";
import {CsmDialogComponent} from "@shared/components/csm-dialog/csm-dialog.component";
import {GroupFormPage} from "@features/access-management/iam/groups/pages/group-form/group-form.page";
import {HasPermissionDirective} from "@shared/directives/permission/has-permission.directive";

@Component({
    selector: 'app-group-list',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        TagModule,
        ToolbarActionComponent,
        Tooltip,
        GroupFormPage,
        CsmDialogComponent,
        HasPermissionDirective
    ],
    templateUrl: './group-list.page.html'
})
export class GroupListPage extends BaseListComponent<Group> {

    protected override resourceName = 'group';

    private service = inject(GroupService);
    private toast = inject(AppToastService);
    private confirm = inject(CsmConfirmService);

    dialogVisible = false;
    selectedId: string | null = null;
    selectedGroups: Group[] = [];

    @ViewChild('dt') dt!: Table;

    fetch() {
        return this.service.getAll();
    }

    // =========================
    // Actions
    // =========================

    openCreate() {
        this.selectedId = null;
        this.dialogVisible = true;
    }

    openEdit(group: Group) {
        this.selectedId = group.id ?? null;
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
