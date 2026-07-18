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
import { AircraftConfigurationRevision } from '@features/masters/aircraft/configuration-revisions/models/configuration-revisions.model';
import { AircraftConfigurationRevisionService } from '@features/masters/aircraft/configuration-revisions/services/configuration-revisions.service';
import { AircraftConfigurationRevisionFormPage } from '@features/masters/aircraft/configuration-revisions/pages/configuration-revisions-form/configuration-revisions-form.page';

@Component({ selector: 'configuration-revisions-list', standalone: true, imports: [CommonModule, TableModule, ButtonModule, TagModule, TooltipModule, ToolbarActionComponent, AppDialogComponent, HasPermissionDirective, AircraftConfigurationRevisionFormPage], templateUrl: './configuration-revisions-list.page.html' })
export class AircraftConfigurationRevisionListPage extends BaseListComponent<AircraftConfigurationRevision> {
    protected override resourceName = 'AIRCRAFTCONFIGURATIONREVISION';
    dialogVisible = false; selectedId: string | null = null; selectedRecords: AircraftConfigurationRevision[] = [];
    private service = inject(AircraftConfigurationRevisionService); private toast = inject(AppToastService); private confirm = inject(AppConfirmService);
    @ViewChild('dt') dt!: Table;
    override fetch() { return this.service.getAll(this.exactFilters); }
    openCreate() { this.selectedId = null; this.dialogVisible = true; }
    openEdit(record: AircraftConfigurationRevision) { this.selectedId = record.id ?? null; this.dialogVisible = true; }
    deleteRecord(record: AircraftConfigurationRevision) {
        if (!record.id) return;
        this.confirm.delete('Delete ' + this.recordLabel(record) + '?', () => {
            this.service.delete(record.id!).subscribe({ next: () => this.refresh(), error: () => this.toast.error('Failed to delete Configuration Revisions') });
        });
    }
    deleteSelectedRecords() {
        if (!this.selectedRecords.length) return;
        this.confirm.delete('Delete ' + this.selectedRecords.length + ' selected record(s)?', () => {
            const requests = this.selectedRecords.filter((record) => !!record.id).map((record) => this.service.delete(record.id!));
            forkJoin(requests).subscribe({ next: () => { this.selectedRecords = []; this.refresh(); }, error: () => this.toast.error('Failed to delete selected Configuration Revisions') });
        });
    }
    exportCSV() { this.dt.exportCSV(); }
    onSaved() { this.dialogVisible = false; this.refresh(); }
    onSearch(value: string) { this.dt.filterGlobal(value, 'contains'); }
    recordLabel(record: AircraftConfigurationRevision) { return String((record as any).name || (record as any).displayName || (record as any).countryName || (record as any).legalName || (record as any).code || record.id || 'record'); }
}
