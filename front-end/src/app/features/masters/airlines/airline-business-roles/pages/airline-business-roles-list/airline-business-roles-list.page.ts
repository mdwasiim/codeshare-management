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
import { AirlineBusinessRole } from '@features/masters/airlines/airline-business-roles/models/airline-business-roles.model';
import { AirlineBusinessRoleService } from '@features/masters/airlines/airline-business-roles/services/airline-business-roles.service';
import { AirlineBusinessRoleFormPage } from '@features/masters/airlines/airline-business-roles/pages/airline-business-roles-form/airline-business-roles-form.page';

@Component({ selector: 'airline-business-roles-list', standalone: true, imports: [CommonModule, TableModule, ButtonModule, TagModule, TooltipModule, ToolbarActionComponent, AppDialogComponent, HasPermissionDirective, AirlineBusinessRoleFormPage], templateUrl: './airline-business-roles-list.page.html' })
export class AirlineBusinessRoleListPage extends BaseListComponent<AirlineBusinessRole> {
    protected override resourceName = 'AIRLINEBUSINESSROLE';
    dialogVisible = false; selectedId: string | null = null; selectedRecords: AirlineBusinessRole[] = [];
    private service = inject(AirlineBusinessRoleService); private toast = inject(AppToastService); private confirm = inject(AppConfirmService);
    @ViewChild('dt') dt!: Table;
    override fetch() { return this.service.getAll(); }
    openCreate() { this.selectedId = null; this.dialogVisible = true; }
    openEdit(record: AirlineBusinessRole) { this.selectedId = record.id ?? null; this.dialogVisible = true; }
    deleteRecord(record: AirlineBusinessRole) {
        if (!record.id) return;
        this.confirm.delete('Delete ' + this.recordLabel(record) + '?', () => {
            this.service.delete(record.id!).subscribe({ next: () => this.refresh(), error: () => this.toast.error('Failed to delete Airline Business Roles') });
        });
    }
    deleteSelectedRecords() {
        if (!this.selectedRecords.length) return;
        this.confirm.delete('Delete ' + this.selectedRecords.length + ' selected record(s)?', () => {
            const requests = this.selectedRecords.filter((record) => !!record.id).map((record) => this.service.delete(record.id!));
            forkJoin(requests).subscribe({ next: () => { this.selectedRecords = []; this.refresh(); }, error: () => this.toast.error('Failed to delete selected Airline Business Roles') });
        });
    }
    exportCSV() { this.dt.exportCSV(); }
    onSaved() { this.dialogVisible = false; this.refresh(); }
    onSearch(value: string) { this.dt.filterGlobal(value, 'contains'); }
    recordLabel(record: AirlineBusinessRole) { return String((record as any).name || (record as any).displayName || (record as any).countryName || (record as any).legalName || (record as any).code || record.id || 'record'); }
}
