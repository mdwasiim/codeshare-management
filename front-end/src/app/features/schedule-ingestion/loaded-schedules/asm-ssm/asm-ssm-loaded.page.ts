import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TableLazyLoadEvent, TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import {
    AnyScheduleFlight,
    LoadedScheduleMessageSummary,
    LoadedScheduleSummary,
    ScheduleFileMetaData,
    ScheduleMessageType,
    ScheduleSubMessage
} from '@features/schedule-ingestion/models/schedule-ingestion.model';
import { AsmSsmIngestionService } from '@features/schedule-ingestion/api/asm-ssm-ingestion.service';

interface Column {
    field: string;
    header: string;
}

@Component({
    selector: 'asm-ssm-loaded',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink, ButtonModule, DialogModule, InputTextModule, SelectModule, TableModule, TagModule, TooltipModule],
    templateUrl: './asm-ssm-loaded.page.html',
    styleUrls: ['./asm-ssm-loaded.page.scss']
})
export class AsmSsmLoadedPage implements OnInit {
    private service = inject(AsmSsmIngestionService);
    private router = inject(Router);

    readonly messageTypes: ScheduleMessageType[] = ['ASM', 'SSM'];
    readonly statuses = ['RECEIVED', 'VALIDATED', 'PARSED', 'LOADED', 'PARTIALLY_LOADED', 'FAILED'];
    readonly sourceTypes = ['LOCAL', 'SFTP', 'EMAIL', 'MQ', 'REST', 'CLOUD'];

    type: ScheduleMessageType = 'ASM';
    airlineCode = '';
    fileName = '';
    processingStatus = '';
    sourceType = '';
    receivedDate = '';

    schedules: LoadedScheduleSummary[] = [];
    messageRows: LoadedScheduleMessageSummary[] = [];
    flights: AnyScheduleFlight[] = [];
    selectedFile: ScheduleFileMetaData | null = null;
    selectedMessageRow: LoadedScheduleMessageSummary | null = null;
    selectedFlight: AnyScheduleFlight | null = null;
    selectedDeis: Record<string, unknown>[] = [];
    deiDialogVisible = false;
    parsedDialogVisible = false;

    loadingSchedules = false;
    loadingFiles = false;
    totalSchedules = 0;
    totalMessages = 0;
    totalFlights = 0;

    readonly flightColumns: Column[] = [
        { field: 'airlineDesignator', header: 'Airline' },
        { field: 'flightNumber', header: 'Flight' },
        { field: 'operationalSuffix', header: 'Suffix' },
        { field: 'flightSequenceNumber', header: 'Flight Seq' },
        { field: 'operationDate', header: 'Operation Date' },
        { field: 'boardPoint', header: 'Board Point' },
        { field: 'offPoint', header: 'Off Point' },
        { field: 'aircraftType', header: 'Aircraft' },
        { field: 'serviceType', header: 'Service' },
        { field: 'aircraftConfiguration', header: 'Configuration' },
        { field: 'bookingDesignator', header: 'Booking' },
        { field: 'periodSummary', header: 'Periods' },
        { field: 'legSummary', header: 'Legs' },
        { field: 'supplementaryInfoSummary', header: 'Supplementary' }
    ];

    readonly deiColumns: Column[] = [
        { field: 'scope', header: 'Scope' },
        { field: 'deiCode', header: 'DEI' },
        { field: 'value', header: 'Value' },
        { field: 'sequenceOrder', header: 'Seq' },
        { field: 'legSequenceNumber', header: 'Leg Seq' },
        { field: 'boardPoint', header: 'Board Point' },
        { field: 'offPoint', header: 'Off Point' },
        { field: 'rawLine', header: 'Raw Line' }
    ];

    ngOnInit(): void {
        this.loadFiles();
    }

    loadSchedules(event?: TableLazyLoadEvent) {
        this.loadingSchedules = true;
        this.service.searchLoadedSchedules(this.type, this.pageParams(event)).subscribe({
            next: (page) => {
                this.schedules = page.content ?? [];
                this.totalSchedules = page.totalElements ?? 0;
                this.loadingSchedules = false;
            },
            error: () => {
                this.schedules = [];
                this.totalSchedules = 0;
                this.loadingSchedules = false;
            }
        });
    }

    loadFiles(event?: TableLazyLoadEvent) {
        this.loadingFiles = true;
        this.clearSelection();
        const receivedRange = this.receivedDateRange();
        const filters = this.cleanParams({
            airlineCode: this.airlineCode,
            fileName: this.fileName,
            processingStatus: this.processingStatus,
            sourceType: this.sourceType,
            receivedFrom: receivedRange.from,
            receivedTo: receivedRange.to
        });

        this.service.searchMessages(this.type, { ...filters, ...this.pageParams(event) }).subscribe({
            next: (page) => {
                this.messageRows = page.content ?? [];
                this.totalMessages = page.totalElements ?? 0;
                this.loadingFiles = false;
            },
            error: () => {
                this.messageRows = [];
                this.totalMessages = 0;
                this.loadingFiles = false;
            }
        });
    }

    loadParsedMessage(row: LoadedScheduleMessageSummary) {
        this.selectedMessageRow = row;
        this.selectedFile = row.file;
        this.flights = this.messageFlights(row);
        this.selectedFlight = null;
        this.selectedDeis = [];
        this.deiDialogVisible = false;
        this.totalFlights = this.flights.length;
    }

    viewDetail(row: LoadedScheduleMessageSummary) {
        const file = row.file;
        this.router.navigate(['/schedule-comparison', file.messageType || this.type, file.fileId], {
            queryParams: {
                fileName: file.fileName || '',
                airlineCode: file.airlineCode || '',
                messageId: row.messageId,
                messageSequenceNumber: row.messageSequenceNumber
            }
        });
    }

    refreshAll() {
        this.loadFiles();
    }

    onTypeChange() {
        this.refreshAll();
    }

    clearFilters() {
        this.airlineCode = '';
        this.fileName = '';
        this.processingStatus = '';
        this.sourceType = '';
        this.receivedDate = '';
        this.loadFiles();
    }

    selectFlight(flight: AnyScheduleFlight) {
        this.selectedFlight = flight;
        this.selectedDeis = this.collectDeis(flight);
        this.deiDialogVisible = true;
    }

    showRawMessage(row: LoadedScheduleMessageSummary) {
        this.selectedMessageRow = row;
        this.flights = this.messageFlights(row);
        this.totalFlights = this.flights.length;
    }

    showParsedMessage(row: LoadedScheduleMessageSummary) {
        this.loadParsedMessage(row);
        this.parsedDialogVisible = true;
    }

    rawMessageBlocks(row: LoadedScheduleMessageSummary | null): ScheduleSubMessage[] {
        return row?.message?.messages ?? [];
    }

    firstFlight(): AnyScheduleFlight | null {
        return this.flights[0] ?? null;
    }

    selectedFlightDeis(): Record<string, unknown>[] {
        const flight = this.firstFlight();
        return flight ? this.collectDeis(flight) : [];
    }

    flightValue(flight: AnyScheduleFlight, field: string) {
        switch (field) {
            case 'periodSummary':
                return this.periodSummary(flight);
            case 'legSummary':
                return this.legSummary(flight);
            case 'supplementaryInfoSummary':
                return flight.supplementaryInfo?.join(', ') || '-';
            default: {
                const value = (flight as Record<string, unknown>)[field];
                return value === undefined || value === null || value === '' ? '-' : value;
            }
        }
    }

    deiValue(dei: Record<string, unknown>, field: string) {
        const value = dei[field];
        return value === undefined || value === null || value === '' ? '-' : value;
    }

    statusSeverity(status?: string): 'success' | 'secondary' | 'danger' | 'info' | 'warn' {
        switch (status) {
            case 'LOADED':
                return 'success';
            case 'FAILED':
                return 'danger';
            case 'PARTIALLY_LOADED':
                return 'warn';
            case 'RECEIVED':
            case 'VALIDATED':
            case 'PARSED':
                return 'info';
            default:
                return 'secondary';
        }
    }

    private collectDeis(flight: AnyScheduleFlight): Record<string, unknown>[] {
        const flightDeis = ((flight.deis ?? []) as Record<string, unknown>[]).map((dei) => ({ scope: 'FLIGHT', ...dei }));
        const legDeis =
            flight.legs?.flatMap((leg) =>
                (leg.deis ?? []).map((dei) => ({
                    scope: dei.scope ?? 'LEG',
                    legSequenceNumber: dei.legSequenceNumber ?? leg.legSequenceNumber,
                    boardPoint: dei.boardPoint ?? leg.boardPoint,
                    offPoint: dei.offPoint ?? leg.offPoint,
                    ...dei
                }))
            ) ?? [];
        return [...flightDeis, ...legDeis];
    }

    private periodSummary(flight: AnyScheduleFlight) {
        if (!flight.periods?.length) return '-';
        return flight.periods.map((period) => `${period.startDate ?? '-'} to ${period.endDate ?? '-'} ${period.daysOfOperation ?? ''}`.trim()).join('; ');
    }

    private legSummary(flight: AnyScheduleFlight) {
        if (!flight.legs?.length) return '-';
        return flight.legs.map((leg) => `${leg.legSequenceNumber ?? '-'}:${leg.boardPoint ?? '-'}-${leg.offPoint ?? '-'} ${leg.departureTime ?? ''}/${leg.arrivalTime ?? ''}`.trim()).join('; ');
    }

    private clearSelection() {
        this.selectedFile = null;
        this.selectedMessageRow = null;
        this.flights = [];
        this.selectedFlight = null;
        this.selectedDeis = [];
        this.deiDialogVisible = false;
        this.parsedDialogVisible = false;
    }

    private messageFlights(row: LoadedScheduleMessageSummary): AnyScheduleFlight[] {
        return (row.message?.messages ?? []).flatMap((message) => message.flights ?? []) as AnyScheduleFlight[];
    }

    private pageParams(event?: TableLazyLoadEvent) {
        const first = event?.first ?? 0;
        const rows = event?.rows ?? 10;
        return { page: Math.floor(first / rows), size: rows };
    }

    private cleanParams(params: Record<string, string>) {
        return Object.entries(params).reduce<Record<string, string>>((acc, [key, value]) => {
            if (value?.trim()) acc[key] = value.trim();
            return acc;
        }, {});
    }

    private receivedDateRange() {
        if (!this.receivedDate?.trim()) return { from: '', to: '' };
        const from = new Date(`${this.receivedDate}T00:00:00`);
        const to = new Date(`${this.receivedDate}T23:59:59.999`);
        if (Number.isNaN(from.getTime()) || Number.isNaN(to.getTime())) return { from: '', to: '' };
        return { from: from.toISOString(), to: to.toISOString() };
    }
}
