import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TableLazyLoadEvent, TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import {
    LoadedScheduleMessageSummary,
    LoadedScheduleSummary,
    OutboundScheduleMessage,
    ScheduleFileMetaData,
    ScheduleMessageType,
    ScheduleSubMessage
} from '@features/schedule-ingestion/models/schedule-ingestion.model';
import { AsmSsmIngestionService } from '@features/schedule-ingestion/api/asm-ssm-ingestion.service';
import { MasterLookupOption, MasterReferenceLookupService } from '@features/masters/shared/master-reference-lookup.service';

@Component({
    selector: 'asm-ssm-loaded',
    standalone: true,
    imports: [CommonModule, FormsModule, ButtonModule, InputTextModule, SelectModule, TableModule, TagModule, TooltipModule],
    templateUrl: './asm-ssm-loaded.page.html',
    styleUrls: ['./asm-ssm-loaded.page.scss']
})
export class AsmSsmLoadedPage implements OnInit {
    private service = inject(AsmSsmIngestionService);
    private router = inject(Router);
    private lookupService = inject(MasterReferenceLookupService);

    messageTypes: MasterLookupOption[] = [];
    statuses: MasterLookupOption[] = [];
    sourceTypes: MasterLookupOption[] = [];
    airlineOptions: MasterLookupOption[] = [];

    type: ScheduleMessageType = 'ASM';
    airlineCode = '';
    fileName = '';
    processingStatus = '';
    sourceType = '';
    receivedDate = '';

    schedules: LoadedScheduleSummary[] = [];
    messageRows: LoadedScheduleMessageSummary[] = [];
    selectedFile: ScheduleFileMetaData | null = null;
    selectedMessageRow: LoadedScheduleMessageSummary | null = null;
    outboundMessage: OutboundScheduleMessage | null = null;
    outboundMessageError = '';

    loadingSchedules = false;
    loadingFiles = false;
    loadingOutboundMessage = false;
    totalSchedules = 0;
    totalMessages = 0;

    ngOnInit(): void {
        this.lookupService.getOptions('airlineCode').subscribe((options) => {
            this.airlineOptions = options;
        });
        this.lookupService.getReferenceOptions<ScheduleMessageType>('INGESTION_MESSAGE_TYPE').subscribe((options) => {
            this.messageTypes = options.filter((option) => option.value === 'ASM' || option.value === 'SSM');
        });
        this.lookupService.getReferenceOptions('SCHEDULE_FILE_PROCESSING_STATUS').subscribe((options) => {
            this.statuses = options;
        });
        this.lookupService.getReferenceOptions('INGESTION_SOURCE_TYPE').subscribe((options) => {
            this.sourceTypes = options;
        });
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

    selectMessage(row: LoadedScheduleMessageSummary) {
        this.selectedMessageRow = row;
        this.selectedFile = row.file;
        this.loadOutboundMessage(row);
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

    rawMessageBlocks(row: LoadedScheduleMessageSummary | null): ScheduleSubMessage[] {
        return row?.message?.messages ?? [];
    }

    inboundRawPayload(row: LoadedScheduleMessageSummary | null): string {
        if (!row) return '';
        const parts = [
            row.message?.rawHeader,
            ...this.rawMessageBlocks(row).map((message) => message.rawMessage)
        ].filter((value): value is string => !!value?.trim());
        return parts.join('\n//\n');
    }

    selectedOutboundMessageId(): string {
        return this.workflowValue(this.selectedMessageRow, 'outboundMessageId');
    }

    selectedChangeSetId(): string {
        return this.workflowValue(this.selectedMessageRow, 'changeSetId');
    }

    selectedImportBatchId(): string {
        return this.workflowValue(this.selectedMessageRow, 'importBatchId') || this.workflowValue(this.selectedMessageRow?.file, 'importBatchId');
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
        this.selectedMessageRow = null;
        this.outboundMessage = null;
        this.outboundMessageError = '';
        this.loadingOutboundMessage = false;
    }

    private loadOutboundMessage(row: LoadedScheduleMessageSummary) {
        this.outboundMessage = null;
        this.outboundMessageError = '';
        const outboundMessageId = this.workflowValue(row, 'outboundMessageId');
        if (!outboundMessageId) return;

        this.loadingOutboundMessage = true;
        this.service.getOutboundMessage(outboundMessageId).subscribe({
            next: (message) => {
                this.outboundMessage = message;
                this.loadingOutboundMessage = false;
            },
            error: () => {
                this.outboundMessageError = 'Outbound message is not available for this inbound message.';
                this.loadingOutboundMessage = false;
            }
        });
    }

    private workflowValue(source: unknown, field: string): string {
        if (!source) return '';
        const value = (source as Record<string, unknown>)[field];
        return typeof value === 'string' ? value : '';
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
