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
import { TimezoneDls } from '@features/masters/geography/timezone-dls/models/timezone-dls.model';
import { TimezoneDlsService } from '@features/masters/geography/timezone-dls/services/timezone-dls.service';
import { TimezoneDlsFormPage } from '@features/masters/geography/timezone-dls/pages/timezone-dls-form/timezone-dls-form.page';

@Component({ selector: 'timezone-dls-list', standalone: true, imports: [CommonModule, TableModule, ButtonModule, TagModule, TooltipModule, ToolbarActionComponent, AppDialogComponent, HasPermissionDirective, TimezoneDlsFormPage], templateUrl: './timezone-dls-list.page.html' })
export class TimezoneDlsListPage extends BaseListComponent<TimezoneDls> {
    protected override resourceName = 'TIMEZONE';
    dialogVisible = false; selectedId: string | null = null; selectedRecords: TimezoneDls[] = [];
    private service = inject(TimezoneDlsService); private toast = inject(AppToastService); private confirm = inject(AppConfirmService);
    @ViewChild('dt') dt!: Table;
    override fetch() { return this.service.getAll(this.exactFilters); }
    openCreate() { this.selectedId = null; this.dialogVisible = true; }
    openEdit(record: TimezoneDls) { this.selectedId = record.id ?? null; this.dialogVisible = true; }
    deleteRecord(record: TimezoneDls) {
        if (!record.id) return;
        this.confirm.delete('Delete ' + this.recordLabel(record) + '?', () => {
            this.service.delete(record.id!).subscribe({ next: () => this.refresh(), error: () => this.toast.error('Failed to delete Time Zone Offset Period') });
        });
    }
    deleteSelectedRecords() {
        if (!this.selectedRecords.length) return;
        this.confirm.delete('Delete ' + this.selectedRecords.length + ' selected record(s)?', () => {
            const requests = this.selectedRecords.filter((record) => !!record.id).map((record) => this.service.delete(record.id!));
            forkJoin(requests).subscribe({ next: () => { this.selectedRecords = []; this.refresh(); }, error: () => this.toast.error('Failed to delete selected Time Zone Offset Periods') });
        });
    }
    exportCSV() { this.dt.exportCSV(); }
    onSaved() { this.dialogVisible = false; this.refresh(); }
    onSearch(value: string) { this.dt.filterGlobal(value, 'contains'); }
    recordLabel(record: TimezoneDls) { return String(record.timezoneIdentifier || record.timezoneId || record.dstStart || record.id || 'record'); }
}
