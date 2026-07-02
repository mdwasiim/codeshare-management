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
import { ReservationBookingDesignator } from '@features/masters/flight/reservation-booking-designators/models/reservation-booking-designators.model';
import { ReservationBookingDesignatorService } from '@features/masters/flight/reservation-booking-designators/services/reservation-booking-designators.service';
import { ReservationBookingDesignatorFormPage } from '@features/masters/flight/reservation-booking-designators/pages/reservation-booking-designators-form/reservation-booking-designators-form.page';

@Component({ selector: 'reservation-booking-designators-list', standalone: true, imports: [CommonModule, TableModule, ButtonModule, TagModule, TooltipModule, ToolbarActionComponent, AppDialogComponent, HasPermissionDirective, ReservationBookingDesignatorFormPage], templateUrl: './reservation-booking-designators-list.page.html' })
export class ReservationBookingDesignatorListPage extends BaseListComponent<ReservationBookingDesignator> {
    protected override resourceName = 'RESERVATIONBOOKINGDESIGNATOR';
    dialogVisible = false; selectedId: string | null = null; selectedRecords: ReservationBookingDesignator[] = [];
    private service = inject(ReservationBookingDesignatorService); private toast = inject(AppToastService); private confirm = inject(AppConfirmService);
    @ViewChild('dt') dt!: Table;
    override fetch() { return this.service.getAll(); }
    openCreate() { this.selectedId = null; this.dialogVisible = true; }
    openEdit(record: ReservationBookingDesignator) { this.selectedId = record.id ?? null; this.dialogVisible = true; }
    deleteRecord(record: ReservationBookingDesignator) {
        if (!record.id) return;
        this.confirm.delete('Delete ' + this.recordLabel(record) + '?', () => {
            this.service.delete(record.id!).subscribe({ next: () => this.refresh(), error: () => this.toast.error('Failed to delete Reservation Booking Designators') });
        });
    }
    deleteSelectedRecords() {
        if (!this.selectedRecords.length) return;
        this.confirm.delete('Delete ' + this.selectedRecords.length + ' selected record(s)?', () => {
            const requests = this.selectedRecords.filter((record) => !!record.id).map((record) => this.service.delete(record.id!));
            forkJoin(requests).subscribe({ next: () => { this.selectedRecords = []; this.refresh(); }, error: () => this.toast.error('Failed to delete selected Reservation Booking Designators') });
        });
    }
    exportCSV() { this.dt.exportCSV(); }
    onSaved() { this.dialogVisible = false; this.refresh(); }
    onSearch(value: string) { this.dt.filterGlobal(value, 'contains'); }
    recordLabel(record: ReservationBookingDesignator) { return String((record as any).name || (record as any).displayName || (record as any).countryName || (record as any).legalName || (record as any).code || record.id || 'record'); }
}
