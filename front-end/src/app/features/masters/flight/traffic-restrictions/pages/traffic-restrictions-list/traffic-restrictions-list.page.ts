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
import { TrafficRestriction } from '@features/masters/flight/traffic-restrictions/models/traffic-restrictions.model';
import { TrafficRestrictionService } from '@features/masters/flight/traffic-restrictions/services/traffic-restrictions.service';
import { TrafficRestrictionFormPage } from '@features/masters/flight/traffic-restrictions/pages/traffic-restrictions-form/traffic-restrictions-form.page';

@Component({ selector: 'traffic-restrictions-list', standalone: true, imports: [CommonModule, TableModule, ButtonModule, TagModule, TooltipModule, ToolbarActionComponent, AppDialogComponent, HasPermissionDirective, TrafficRestrictionFormPage], templateUrl: './traffic-restrictions-list.page.html' })
export class TrafficRestrictionListPage extends BaseListComponent<TrafficRestriction> {
    protected override resourceName = 'TRAFFICRESTRICTION';
    dialogVisible = false; selectedId: string | null = null; selectedRecords: TrafficRestriction[] = [];
    private service = inject(TrafficRestrictionService); private toast = inject(AppToastService); private confirm = inject(AppConfirmService);
    @ViewChild('dt') dt!: Table;
    override fetch() { return this.service.getAll(); }
    openCreate() { this.selectedId = null; this.dialogVisible = true; }
    openEdit(record: TrafficRestriction) { this.selectedId = record.id ?? null; this.dialogVisible = true; }
    deleteRecord(record: TrafficRestriction) {
        if (!record.id) return;
        this.confirm.delete('Delete ' + this.recordLabel(record) + '?', () => {
            this.service.delete(record.id!).subscribe({ next: () => this.refresh(), error: () => this.toast.error('Failed to delete Traffic Restrictions') });
        });
    }
    deleteSelectedRecords() {
        if (!this.selectedRecords.length) return;
        this.confirm.delete('Delete ' + this.selectedRecords.length + ' selected record(s)?', () => {
            const requests = this.selectedRecords.filter((record) => !!record.id).map((record) => this.service.delete(record.id!));
            forkJoin(requests).subscribe({ next: () => { this.selectedRecords = []; this.refresh(); }, error: () => this.toast.error('Failed to delete selected Traffic Restrictions') });
        });
    }
    exportCSV() { this.dt.exportCSV(); }
    onSaved() { this.dialogVisible = false; this.refresh(); }
    onSearch(value: string) { this.dt.filterGlobal(value, 'contains'); }
    recordLabel(record: TrafficRestriction) { return String((record as any).name || (record as any).displayName || (record as any).countryName || (record as any).legalName || (record as any).code || record.id || 'record'); }
}
