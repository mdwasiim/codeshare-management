import { Component, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs';
import { ButtonModule } from 'primeng/button';
import { Table, TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { BaseListComponent } from '@shared/components/base/base-list.component';
import { ToolbarActionComponent } from '@shared/components/toolbar/toolbar-action.component';
import { AppConfirmService } from '@services/app-confirm.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AppDialogComponent } from '@shared/components/app-dialog/app-dialog.component';
import { HasPermissionDirective } from '@shared/directives/permission/has-permission.directive';
import { SchedulePriority } from '@features/masters/schedule/schedule-priorities/models/schedule-priorities.model';
import { SchedulePriorityService } from '@features/masters/schedule/schedule-priorities/services/schedule-priorities.service';
import { SchedulePriorityFormPage } from '@features/masters/schedule/schedule-priorities/pages/schedule-priorities-form/schedule-priorities-form.page';

@Component({ selector: 'schedule-priorities-list', standalone: true, imports: [CommonModule, TableModule, ButtonModule, TagModule, TooltipModule, ToolbarActionComponent, AppDialogComponent, HasPermissionDirective, SchedulePriorityFormPage], templateUrl: './schedule-priorities-list.page.html' })
export class SchedulePriorityListPage extends BaseListComponent<SchedulePriority> {
    protected override resourceName = 'SCHEDULEPRIORITY';
    dialogVisible = false; selectedId: string | null = null; selectedRecords: SchedulePriority[] = [];
    private service = inject(SchedulePriorityService); private toast = inject(AppToastService); private confirm = inject(AppConfirmService);
    @ViewChild('dt') dt!: Table;
    override fetch() { return this.service.getAll(this.exactFilters); }
    openCreate() { this.selectedId = null; this.dialogVisible = true; }
    openEdit(record: SchedulePriority) { this.selectedId = record.id ?? null; this.dialogVisible = true; }
    deleteRecord(record: SchedulePriority) {
        if (!record.id) return;
        this.confirm.delete('Delete ' + this.recordLabel(record) + '?', () => {
            this.service.delete(record.id!).subscribe({ next: () => this.refresh(), error: () => this.toast.error('Failed to delete Schedule Priorities') });
        });
    }
    deleteSelectedRecords() {
        if (!this.selectedRecords.length) return;
        this.confirm.delete('Delete ' + this.selectedRecords.length + ' selected record(s)?', () => {
            const requests = this.selectedRecords.filter((record) => !!record.id).map((record) => this.service.delete(record.id!));
            forkJoin(requests).subscribe({ next: () => { this.selectedRecords = []; this.refresh(); }, error: () => this.toast.error('Failed to delete selected Schedule Priorities') });
        });
    }
    exportCSV() { this.dt.exportCSV(); }
    onSaved() { this.dialogVisible = false; this.refresh(); }
    onSearch(value: string) { this.dt.filterGlobal(value, 'contains'); }
    recordLabel(record: SchedulePriority) { return String((record as any).name || (record as any).displayName || (record as any).countryName || (record as any).legalName || (record as any).code || record.id || 'record'); }
}
