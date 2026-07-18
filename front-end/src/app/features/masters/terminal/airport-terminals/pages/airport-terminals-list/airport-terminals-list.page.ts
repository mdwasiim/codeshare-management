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
import { AirportTerminal } from '@features/masters/terminal/airport-terminals/models/airport-terminals.model';
import { AirportTerminalService } from '@features/masters/terminal/airport-terminals/services/airport-terminals.service';
import { AirportTerminalFormPage } from '@features/masters/terminal/airport-terminals/pages/airport-terminals-form/airport-terminals-form.page';

@Component({ selector: 'airport-terminals-list', standalone: true, imports: [CommonModule, TableModule, ButtonModule, TagModule, TooltipModule, ToolbarActionComponent, AppDialogComponent, HasPermissionDirective, AirportTerminalFormPage], templateUrl: './airport-terminals-list.page.html' })
export class AirportTerminalListPage extends BaseListComponent<AirportTerminal> {
    protected override resourceName = 'AIRPORTTERMINAL';
    dialogVisible = false; selectedId: string | null = null; selectedRecords: AirportTerminal[] = [];
    private service = inject(AirportTerminalService); private toast = inject(AppToastService); private confirm = inject(AppConfirmService);
    @ViewChild('dt') dt!: Table;
    override fetch() { return this.service.getAll(this.exactFilters); }
    openCreate() { this.selectedId = null; this.dialogVisible = true; }
    openEdit(record: AirportTerminal) { this.selectedId = record.id ?? null; this.dialogVisible = true; }
    deleteRecord(record: AirportTerminal) {
        if (!record.id) return;
        this.confirm.delete('Delete ' + this.recordLabel(record) + '?', () => {
            this.service.delete(record.id!).subscribe({ next: () => this.refresh(), error: () => this.toast.error('Failed to delete Airport Terminals') });
        });
    }
    deleteSelectedRecords() {
        if (!this.selectedRecords.length) return;
        this.confirm.delete('Delete ' + this.selectedRecords.length + ' selected record(s)?', () => {
            const requests = this.selectedRecords.filter((record) => !!record.id).map((record) => this.service.delete(record.id!));
            forkJoin(requests).subscribe({ next: () => { this.selectedRecords = []; this.refresh(); }, error: () => this.toast.error('Failed to delete selected Airport Terminals') });
        });
    }
    exportCSV() { this.dt.exportCSV(); }
    onSaved() { this.dialogVisible = false; this.refresh(); }
    onSearch(value: string) { this.dt.filterGlobal(value, 'contains'); }
    recordLabel(record: AirportTerminal) { return String((record as any).name || (record as any).displayName || (record as any).countryName || (record as any).legalName || (record as any).code || record.id || 'record'); }
}
