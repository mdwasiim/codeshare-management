import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TableLazyLoadEvent, TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import {
    AnyScheduleFlight,
    LoadedScheduleDetail,
    LoadedScheduleSummary,
    ScheduleFileMetaData,
    ScheduleMessageType
} from '@features/schedule-ingestion/models/schedule-ingestion.model';
import { ScheduleIngestionService } from '@features/schedule-ingestion/services/schedule-ingestion.service';

interface FlightColumn {
    field: string;
    header: string;
}

@Component({
    selector: 'loaded-schedules',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink, ButtonModule, DialogModule, InputTextModule, SelectModule, TableModule, TagModule, TooltipModule],
    templateUrl: './loaded-schedules.page.html',
    styleUrls: ['./loaded-schedules.page.scss']
})
export class LoadedSchedulesPage implements OnInit {
    private service = inject(ScheduleIngestionService);
    private route = inject(ActivatedRoute);

    readonly asmSsmTypes: ScheduleMessageType[] = ['ASM', 'SSM'];
    readonly statuses = ['RECEIVED', 'VALIDATED', 'PARSED', 'LOADED', 'PARTIALLY_LOADED', 'FAILED'];
    readonly sourceTypes = ['LOCAL', 'SFTP', 'EMAIL', 'MQ', 'REST', 'CLOUD'];

    mode: 'SSIM' | 'ASM_SSM' = 'SSIM';
    type: ScheduleMessageType = 'SSIM';
    airlineCode = '';
    fileName = '';
    processingStatus = '';
    sourceType = '';
    receivedDate = '';

    schedules: LoadedScheduleSummary[] = [];
    files: ScheduleFileMetaData[] = [];
    flights: AnyScheduleFlight[] = [];
    selectedFile: ScheduleFileMetaData | null = null;
    selectedDetail: LoadedScheduleDetail | unknown | null = null;
    selectedFlight: AnyScheduleFlight | null = null;
    selectedDeis: Record<string, unknown>[] = [];
    deiDialogVisible = false;
    detailVisible = false;

    loadingSchedules = false;
    loadingFiles = false;
    loadingFlights = false;
    loadingDetail = false;
    totalSchedules = 0;
    totalFiles = 0;
    totalFlights = 0;

    readonly ssimFlightColumns: FlightColumn[] = [
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
        { field: 'departureUtcVariation', header: 'Dep UTC Var' },
        { field: 'departureTerminal', header: 'Dep Terminal' },
        { field: 'arrivalStation', header: 'Arrival' },
        { field: 'aircraftSta', header: 'Aircraft STA' },
        { field: 'passengerSta', header: 'Pax STA' },
        { field: 'arrivalUtcVariation', header: 'Arr UTC Var' },
        { field: 'arrivalTerminal', header: 'Arr Terminal' },
        { field: 'aircraftType', header: 'Aircraft' },
        { field: 'passengerReservationBookingDesignator', header: 'Booking' },
        { field: 'passengerReservationBookingModifier', header: 'Booking Mod' },
        { field: 'mealServiceNote', header: 'Meal' },
        { field: 'jointOperationAirlineDesignators', header: 'Joint Ops' },
        { field: 'minimumConnectingTimeStatus', header: 'MCT' },
        { field: 'secureFlightIndicator', header: 'Secure' },
        { field: 'itineraryVariationOverflow', header: 'Itin Overflow' },
        { field: 'aircraftOwner', header: 'Owner' },
        { field: 'cockpitCrewEmployer', header: 'Cockpit Crew' },
        { field: 'cabinCrewEmployer', header: 'Cabin Crew' },
        { field: 'onwardAirlineDesignator', header: 'Onward Airline' },
        { field: 'onwardFlightNumber', header: 'Onward Flight' },
        { field: 'aircraftRotationLayover', header: 'Layover' },
        { field: 'onwardOperationalSuffix', header: 'Onward Suffix' },
        { field: 'flightTransitLayover', header: 'Transit' },
        { field: 'operatingAirlineDisclosure', header: 'Disclosure' },
        { field: 'trafficRestrictionCode', header: 'Traffic Restriction' },
        { field: 'trafficRestrictionOverflow', header: 'TR Overflow' },
        { field: 'aircraftConfigurationVersion', header: 'Config Version' },
        { field: 'dateVariation', header: 'Date Var' },
        { field: 'recordSerialNumber', header: 'Serial' }
    ];

    readonly scheduleFlightColumns: FlightColumn[] = [
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

    ngOnInit(): void {
        this.mode = this.route.snapshot.data['mode'] === 'ASM_SSM' ? 'ASM_SSM' : 'SSIM';
        this.type = this.mode === 'SSIM' ? 'SSIM' : 'ASM';
        this.loadSchedules();
        this.loadFiles();
    }

    get title() {
        return this.mode === 'SSIM' ? 'SSIM' : 'Schedule (ASM/SSM)';
    }

    get subtitle() {
        return this.mode === 'SSIM' ? 'List SSIM metadata and inspect loaded SSIM schedules.' : 'List schedule metadata and inspect loaded ASM or SSM messages.';
    }

    get actionRoute() {
        return this.mode === 'SSIM' ? '/ssim-ingestion/actions' : '/asm-ssm-ingestion/actions';
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
        this.selectedFile = null;
        this.flights = [];
        this.selectedFlight = null;
        this.selectedDeis = [];
        this.deiDialogVisible = false;
        this.selectedDetail = null;
        this.detailVisible = false;
        const receivedRange = this.receivedDateRange();
        const filters = this.cleanParams({
            airlineCode: this.airlineCode,
            fileName: this.fileName,
            processingStatus: this.processingStatus,
            sourceType: this.sourceType,
            receivedFrom: receivedRange.from,
            receivedTo: receivedRange.to
        });

        this.service.searchFiles(this.type, { ...filters, ...this.pageParams(event) }).subscribe({
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

        this.service.searchFlights(this.type, file.fileId, this.pageParams(event)).subscribe({
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
        this.selectedFile = file;
        this.detailVisible = true;
        this.loadingDetail = true;

        this.service.getLoadedScheduleDetail(this.type, file.fileId).subscribe({
            next: (detail) => {
                this.selectedDetail = detail;
                this.loadingDetail = false;
            },
            error: () => {
                this.service.getFileSchedule(this.type, file.fileId).subscribe({
                    next: (detail) => {
                        this.selectedDetail = detail;
                        this.loadingDetail = false;
                    },
                    error: () => {
                        this.selectedDetail = null;
                        this.loadingDetail = false;
                    }
                });
            }
        });
    }

    refreshAll() {
        this.loadSchedules();
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

    formatDetail() {
        return JSON.stringify(this.selectedDetail, null, 2);
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

    flightAirline(flight: AnyScheduleFlight) {
        return flight.airlineCode || flight.airlineDesignator || '-';
    }

    flightOrigin(flight: AnyScheduleFlight) {
        return flight.departureStation || flight.boardPoint || '-';
    }

    flightDestination(flight: AnyScheduleFlight) {
        return flight.arrivalStation || flight.offPoint || '-';
    }

    get flightColumns() {
        return this.mode === 'SSIM' ? this.ssimFlightColumns : this.scheduleFlightColumns;
    }

    get deiColumns(): FlightColumn[] {
        if (this.mode === 'SSIM') {
            return [
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
        }

        return [
            { field: 'scope', header: 'Scope' },
            { field: 'deiCode', header: 'DEI' },
            { field: 'value', header: 'Value' },
            { field: 'sequenceOrder', header: 'Seq' },
            { field: 'legSequenceNumber', header: 'Leg Seq' },
            { field: 'boardPoint', header: 'Board Point' },
            { field: 'offPoint', header: 'Off Point' },
            { field: 'rawLine', header: 'Raw Line' }
        ];
    }

    selectFlight(flight: AnyScheduleFlight) {
        this.selectedFlight = flight;
        this.selectedDeis = this.collectDeis(flight);
        this.deiDialogVisible = true;
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

    private collectDeis(flight: AnyScheduleFlight): Record<string, unknown>[] {
        const flightDeis = ((flight.deis ?? []) as Record<string, unknown>[]).map((dei) => ({
            scope: this.mode === 'SSIM' ? undefined : 'FLIGHT',
            ...dei
        }));

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

    private pageParams(event?: TableLazyLoadEvent) {
        const first = event?.first ?? 0;
        const rows = event?.rows ?? 10;
        return {
            page: Math.floor(first / rows),
            size: rows
        };
    }

    private cleanParams(params: Record<string, string>) {
        return Object.entries(params).reduce<Record<string, string>>((acc, [key, value]) => {
            if (value?.trim()) {
                acc[key] = value.trim();
            }
            return acc;
        }, {});
    }

    private receivedDateRange() {
        if (!this.receivedDate?.trim()) {
            return { from: '', to: '' };
        }

        const from = new Date(`${this.receivedDate}T00:00:00`);
        const to = new Date(`${this.receivedDate}T23:59:59.999`);

        if (Number.isNaN(from.getTime()) || Number.isNaN(to.getTime())) {
            return { from: '', to: '' };
        }

        return {
            from: from.toISOString(),
            to: to.toISOString()
        };
    }
}
