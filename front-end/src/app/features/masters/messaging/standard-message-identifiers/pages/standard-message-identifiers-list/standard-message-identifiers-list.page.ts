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
import { StandardMessageIdentifier } from '@features/masters/messaging/standard-message-identifiers/models/standard-message-identifiers.model';
import { StandardMessageIdentifierService } from '@features/masters/messaging/standard-message-identifiers/services/standard-message-identifiers.service';
import { StandardMessageIdentifierFormPage } from '@features/masters/messaging/standard-message-identifiers/pages/standard-message-identifiers-form/standard-message-identifiers-form.page';

@Component({ selector: 'standard-message-identifiers-list', standalone: true, imports: [CommonModule, TableModule, ButtonModule, TagModule, TooltipModule, ToolbarActionComponent, AppDialogComponent, HasPermissionDirective, StandardMessageIdentifierFormPage], templateUrl: './standard-message-identifiers-list.page.html' })
export class StandardMessageIdentifierListPage extends BaseListComponent<StandardMessageIdentifier> {
    protected override resourceName = 'STANDARDMESSAGEIDENTIFIER';
    dialogVisible = false; selectedId: string | null = null; selectedRecords: StandardMessageIdentifier[] = [];
    private service = inject(StandardMessageIdentifierService); private toast = inject(AppToastService); private confirm = inject(AppConfirmService);
    @ViewChild('dt') dt!: Table;
    override fetch() { return this.service.getAll(); }
    openCreate() { this.selectedId = null; this.dialogVisible = true; }
    openEdit(record: StandardMessageIdentifier) { this.selectedId = record.id ?? null; this.dialogVisible = true; }
    deleteRecord(record: StandardMessageIdentifier) {
        if (!record.id) return;
        this.confirm.delete('Delete ' + this.recordLabel(record) + '?', () => {
            this.service.delete(record.id!).subscribe({ next: () => this.refresh(), error: () => this.toast.error('Failed to delete Standard Message Identifiers') });
        });
    }
    deleteSelectedRecords() {
        if (!this.selectedRecords.length) return;
        this.confirm.delete('Delete ' + this.selectedRecords.length + ' selected record(s)?', () => {
            const requests = this.selectedRecords.filter((record) => !!record.id).map((record) => this.service.delete(record.id!));
            forkJoin(requests).subscribe({ next: () => { this.selectedRecords = []; this.refresh(); }, error: () => this.toast.error('Failed to delete selected Standard Message Identifiers') });
        });
    }
    exportCSV() { this.dt.exportCSV(); }
    onSaved() { this.dialogVisible = false; this.refresh(); }
    onSearch(value: string) { this.dt.filterGlobal(value, 'contains'); }
    recordLabel(record: StandardMessageIdentifier) { return String((record as any).name || (record as any).displayName || (record as any).countryName || (record as any).legalName || (record as any).code || record.id || 'record'); }
}
