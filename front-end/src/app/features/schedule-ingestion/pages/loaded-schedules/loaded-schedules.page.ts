import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TableLazyLoadEvent, TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { TextareaModule } from 'primeng/textarea';
import { TooltipModule } from 'primeng/tooltip';
import {
    AnyScheduleFlight,
    LoadedScheduleDetail,
    LoadedScheduleSummary,
    ScheduleAction,
    ScheduleFileMetaData,
    ScheduleMessageType,
    ScheduleValidationMessage
} from '@features/schedule-ingestion/models/schedule-ingestion.model';
import { ScheduleIngestionService } from '@features/schedule-ingestion/services/schedule-ingestion.service';

@Component({
    selector: 'loaded-schedules',
    standalone: true,
    imports: [CommonModule, FormsModule, ButtonModule, InputTextModule, SelectModule, TableModule, TagModule, TextareaModule, TooltipModule],
    templateUrl: './loaded-schedules.page.html',
    styleUrls: ['./loaded-schedules.page.scss']
})
export class LoadedSchedulesPage implements OnInit {
    private service = inject(ScheduleIngestionService);
    private route = inject(ActivatedRoute);

    readonly asmSsmTypes: ScheduleMessageType[] = ['ASM', 'SSM'];
    readonly statuses = ['RECEIVED', 'VALIDATED', 'PARSED', 'LOADED', 'PARTIALLY_LOADED', 'FAILED'];
    readonly actions: { label: string; value: ScheduleAction }[] = [
        { label: 'Validate', value: 'validate' },
        { label: 'Parse', value: 'parse' },
        { label: 'Ingest', value: 'ingest' }
    ];

    mode: 'SSIM' | 'ASM_SSM' = 'SSIM';
    type: ScheduleMessageType = 'SSIM';
    airlineCode = '';
    fileName = '';
    processingStatus = '';
    action: ScheduleAction = 'validate';
    messageFileName = 'schedule-message.txt';
    content = '';
    selectedMessageFile: File | null = null;
    queueFile: File | null = null;
    uploadResult: Record<string, unknown> | null = null;

    schedules: LoadedScheduleSummary[] = [];
    files: ScheduleFileMetaData[] = [];
    flights: AnyScheduleFlight[] = [];
    selectedFile: ScheduleFileMetaData | null = null;
    selectedDetail: LoadedScheduleDetail | unknown | null = null;

    loadingUpload = false;
    loadingSchedules = false;
    loadingFiles = false;
    loadingFlights = false;
    loadingDetail = false;
    totalSchedules = 0;
    totalFiles = 0;
    totalFlights = 0;

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
        return this.mode === 'SSIM' ? 'Upload SSIM files, list SSIM metadata, and inspect loaded SSIM schedules.' : 'Upload ASM or SSM files, list schedule metadata, and inspect loaded schedule messages.';
    }

    get validationMessages(): ScheduleValidationMessage[] {
        const result = this.uploadResult;
        if (!result || !('messages' in result)) return [];
        return (result['messages'] as ScheduleValidationMessage[] | undefined) ?? [];
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
        this.selectedDetail = null;
        const filters = this.cleanParams({
            airlineCode: this.airlineCode,
            fileName: this.fileName,
            processingStatus: this.processingStatus
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

        this.service.searchFlights(this.type, file.fileId, this.pageParams(event)).subscribe({
            next: (page) => {
                this.flights = page.content ?? [];
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

    refreshAll() {
        this.loadSchedules();
        this.loadFiles();
    }

    onTypeChange() {
        this.refreshAll();
    }

    onMessageFileSelected(event: Event) {
        const input = event.target as HTMLInputElement;
        this.selectedMessageFile = input.files?.[0] ?? null;
        if (this.selectedMessageFile) {
            this.messageFileName = this.selectedMessageFile.name;
        }
    }

    onQueueFileSelected(event: Event) {
        const input = event.target as HTMLInputElement;
        this.queueFile = input.files?.[0] ?? null;
    }

    submitMessage() {
        if (!this.airlineCode.trim() || (!this.content.trim() && !this.selectedMessageFile)) return;

        this.loadingUpload = true;
        const request = this.selectedMessageFile
            ? this.service.submitFile(this.action, this.type, this.airlineCode.trim(), this.selectedMessageFile)
            : this.service.submitMessage(this.action, this.type, this.airlineCode.trim(), this.messageFileName.trim(), this.content);

        request.subscribe({
            next: (result) => {
                this.uploadResult = result as unknown as Record<string, unknown>;
                this.loadingUpload = false;
                this.refreshAll();
            },
            error: (error) => {
                this.uploadResult = error?.error ?? { error: error?.message ?? 'Request failed' };
                this.loadingUpload = false;
            }
        });
    }

    queueUpload() {
        if (!this.airlineCode.trim() || !this.queueFile) return;

        this.loadingUpload = true;
        this.service.upload(this.queueFile, this.airlineCode.trim(), this.type).subscribe({
            next: (result) => {
                this.uploadResult = result as Record<string, unknown>;
                this.loadingUpload = false;
                this.refreshAll();
            },
            error: (error) => {
                this.uploadResult = error?.error ?? { error: error?.message ?? 'Upload failed' };
                this.loadingUpload = false;
            }
        });
    }

    formatDetail() {
        return JSON.stringify(this.selectedDetail, null, 2);
    }

    formatUploadResult() {
        return JSON.stringify(this.uploadResult, null, 2);
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
}
