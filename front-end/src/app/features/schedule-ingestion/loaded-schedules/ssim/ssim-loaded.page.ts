import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TableLazyLoadEvent, TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { AnyScheduleFlight, LoadedScheduleSummary, ScheduleFileMetaData } from '@features/schedule-ingestion/models/schedule-ingestion.model';
import { SsimIngestionService } from '@features/schedule-ingestion/api/ssim-ingestion.service';
import { MasterLookupOption, MasterReferenceLookupService } from '@features/masters/shared/master-reference-lookup.service';

interface Column {
    field: string;
    header: string;
}

interface PipelineStage {
    label: string;
    icon: string;
    status: 'done' | 'active' | 'waiting';
    count: number | null;
}

@Component({
    selector: 'ssim-loaded',
    standalone: true,
    imports: [CommonModule, FormsModule, ButtonModule, DialogModule, InputTextModule, SelectModule, TableModule, TagModule, TooltipModule],
    templateUrl: './ssim-loaded.page.html',
    styleUrls: ['./ssim-loaded.page.scss']
})
export class SsimLoadedPage implements OnInit {
    private service = inject(SsimIngestionService);
    private router = inject(Router);
    private lookupService = inject(MasterReferenceLookupService);

    readonly statuses = ['RECEIVED', 'VALIDATED', 'PARSED', 'LOADED', 'PARTIALLY_LOADED', 'FAILED'];
    readonly sourceTypes = ['LOCAL', 'SFTP', 'EMAIL', 'MQ', 'REST', 'CLOUD'];
    airlineOptions: MasterLookupOption[] = [];

    airlineCode = '';
    fileName = '';
    processingStatus = '';
    sourceType = '';
    receivedDate = '';

    schedules: LoadedScheduleSummary[] = [];
    files: ScheduleFileMetaData[] = [];
    flights: AnyScheduleFlight[] = [];
    selectedFile: ScheduleFileMetaData | null = null;
    selectedFlight: AnyScheduleFlight | null = null;
    selectedDeis: Record<string, unknown>[] = [];
    deiDialogVisible = false;

    loadingSchedules = false;
    loadingFiles = false;
    loadingFlights = false;
    totalSchedules = 0;
    totalFiles = 0;
    totalFlights = 0;

    readonly flightColumns: Column[] = [
        { field: 'recordType', header: 'Record Type' },
        { field: 'operationalSuffix', header: 'Suffix' },
        { field: 'airlineCode', header: 'Airline' },
        { field: 'flightNumber', header: 'Flight' },
        { field: 'itineraryVariationIdentifier', header: 'Itin Var' },
        { field: 'legSequenceNumber', header: 'Leg Seq' },
        { field: 'serviceType', header: 'Service' },
        { field: 'operatingPeriodStartRaw', header: 'Period Start' },
        { field: 'operatingPeriodEndRaw', header: 'Period End' },
        { field: 'operatingDays', header: 'Days' },
        { field: 'frequencyRate', header: 'Freq' },
        { field: 'departureStation', header: 'Departure' },
        { field: 'passengerStd', header: 'Pax STD' },
        { field: 'aircraftStd', header: 'Aircraft STD' },
        { field: 'arrivalStation', header: 'Arrival' },
        { field: 'aircraftSta', header: 'Aircraft STA' },
        { field: 'passengerSta', header: 'Pax STA' },
        { field: 'arrivalTerminal', header: 'Arr Terminal' },
        { field: 'aircraftType', header: 'Aircraft' },
        { field: 'passengerReservationBookingDesignator', header: 'Booking' },
        { field: 'recordSerialNumber', header: 'Serial' }
    ];

    readonly deiColumns: Column[] = [
        { field: 'recordType', header: 'Record Type' },
        { field: 'airlineCode', header: 'Airline' },
        { field: 'flightNumber', header: 'Flight' },
        { field: 'legSequenceNumber', header: 'Leg Seq' },
        { field: 'serviceType', header: 'Service' },
        { field: 'boardPointIndicator', header: 'Board Ind' },
        { field: 'offPointIndicator', header: 'Off Ind' },
        { field: 'dataElementIdentifier', header: 'DEI' },
        { field: 'boardPoint', header: 'Board Point' },
        { field: 'offPoint', header: 'Off Point' },
        { field: 'deiData', header: 'Data' },
        { field: 'recordSerialNumber', header: 'Serial' }
    ];

    get loadedFileCount(): number {
        return this.files.filter((file) => file.processingStatus === 'LOADED').length;
    }

    get failedFileCount(): number {
        return this.files.filter((file) => file.processingStatus === 'FAILED').length;
    }

    get partialFileCount(): number {
        return this.files.filter((file) => file.processingStatus === 'PARTIALLY_LOADED').length;
    }

    get validationReadyCount(): number {
        return this.files.filter((file) => ['VALIDATED', 'PARSED', 'LOADED', 'PARTIALLY_LOADED'].includes(file.processingStatus || '')).length;
    }

    get pipelineStages(): PipelineStage[] {
        const received = this.totalFiles || this.files.length;
        const imported = this.loadedFileCount + this.partialFileCount;
        return [
            { label: 'Receive', icon: 'pi pi-inbox', status: received ? 'done' : 'waiting', count: received },
            { label: 'Store', icon: 'pi pi-database', status: imported ? 'done' : received ? 'active' : 'waiting', count: imported },
            { label: 'Validate', icon: 'pi pi-shield', status: this.validationReadyCount ? 'done' : received ? 'active' : 'waiting', count: this.validationReadyCount },
            { label: 'Parse', icon: 'pi pi-sitemap', status: this.totalSchedules ? 'done' : this.validationReadyCount ? 'active' : 'waiting', count: this.totalSchedules || null },
            { label: 'Business', icon: 'pi pi-check-square', status: this.selectedFile ? 'active' : 'waiting', count: null },
            { label: 'Compare', icon: 'pi pi-code-branch', status: this.selectedFile ? 'active' : 'waiting', count: null },
            { label: 'Approve', icon: 'pi pi-verified', status: 'waiting', count: null },
            { label: 'Distribute', icon: 'pi pi-send', status: 'waiting', count: null }
        ];
    }

    ngOnInit(): void {
        this.lookupService.getOptions('airlineCode').subscribe((options) => {
            this.airlineOptions = options;
        });
        this.loadSchedules();
        this.loadFiles();
    }

    loadSchedules(event?: TableLazyLoadEvent) {
        this.loadingSchedules = true;
        this.service.searchLoadedSchedules(this.pageParams(event)).subscribe({
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

        this.service.searchFiles({ ...filters, ...this.pageParams(event) }).subscribe({
            next: (page) => {
                this.files = page.content ?? [];
                this.totalFiles = page.totalElements ?? 0;
                this.loadingFiles = false;
            },
            error: () => {
                this.files = [];
                this.totalFiles = 0;
                this.loadingFiles = false;
            }
        });
    }

    loadFlights(file: ScheduleFileMetaData, event?: TableLazyLoadEvent) {
        this.selectedFile = file;
        this.loadingFlights = true;
        this.service.searchFlights(file.fileId, this.pageParams(event)).subscribe({
            next: (page) => {
                this.flights = page.content ?? [];
                this.selectedFlight = null;
                this.selectedDeis = [];
                this.deiDialogVisible = false;
                this.totalFlights = page.totalElements ?? 0;
                this.loadingFlights = false;
            },
            error: () => {
                this.flights = [];
                this.totalFlights = 0;
                this.loadingFlights = false;
            }
        });
    }

    viewDetail(file: ScheduleFileMetaData) {
        this.router.navigate(['/schedule-comparison', file.messageType || 'SSIM', file.fileId], {
            queryParams: { fileName: file.fileName || '', airlineCode: file.airlineCode || '' }
        });
    }

    refreshAll() {
        this.loadSchedules();
        this.loadFiles();
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
        this.selectedDeis = ((flight.deis ?? []) as Record<string, unknown>[]);
        this.deiDialogVisible = true;
    }

    flightValue(flight: AnyScheduleFlight, field: string) {
        const value = (flight as Record<string, unknown>)[field];
        return value === undefined || value === null || value === '' ? '-' : value;
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

    private clearSelection() {
        this.selectedFile = null;
        this.flights = [];
        this.selectedFlight = null;
        this.selectedDeis = [];
        this.deiDialogVisible = false;
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
