import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { TextareaModule } from 'primeng/textarea';
import { TooltipModule } from 'primeng/tooltip';
import { ScheduleAction, ScheduleMessageType, ScheduleValidationMessage, ScheduleValidationResponse } from '@features/schedule-ingestion/models/schedule-ingestion.model';
import { ScheduleIngestionService } from '@features/schedule-ingestion/services/schedule-ingestion.service';

@Component({
    selector: 'schedule-upload',
    standalone: true,
    imports: [CommonModule, FormsModule, ButtonModule, InputTextModule, SelectModule, TableModule, TagModule, TextareaModule, TooltipModule],
    templateUrl: './schedule-upload.page.html',
    styleUrls: ['./schedule-upload.page.scss']
})
export class ScheduleUploadPage {
    private service = inject(ScheduleIngestionService);

    readonly messageTypes: ScheduleMessageType[] = ['SSIM', 'ASM', 'SSM'];
    readonly actions: { label: string; value: ScheduleAction }[] = [
        { label: 'Validate', value: 'validate' },
        { label: 'Parse', value: 'parse' },
        { label: 'Ingest', value: 'ingest' }
    ];

    airlineCode = '';
    messageType: ScheduleMessageType = 'SSIM';
    action: ScheduleAction = 'validate';
    fileName = 'schedule-message.txt';
    content = '';
    selectedFile: File | null = null;
    queueFile: File | null = null;
    loading = signal(false);
    response = signal<ScheduleValidationResponse | Record<string, unknown> | null>(null);

    readonly validationMessages = computed<ScheduleValidationMessage[]>(() => {
        const result = this.response();
        if (!result || !('messages' in result)) return [];
        return (result.messages as ScheduleValidationMessage[] | undefined) ?? [];
    });

    onMessageFileSelected(event: Event) {
        const input = event.target as HTMLInputElement;
        this.selectedFile = input.files?.[0] ?? null;
        if (this.selectedFile) {
            this.fileName = this.selectedFile.name;
        }
    }

    onQueueFileSelected(event: Event) {
        const input = event.target as HTMLInputElement;
        this.queueFile = input.files?.[0] ?? null;
    }

    submitMessage() {
        if (!this.airlineCode.trim() || (!this.content.trim() && !this.selectedFile)) return;

        this.loading.set(true);
        const request = this.selectedFile
            ? this.service.submitFile(this.action, this.messageType, this.airlineCode.trim(), this.selectedFile)
            : this.service.submitMessage(this.action, this.messageType, this.airlineCode.trim(), this.fileName.trim(), this.content);

        request.subscribe({
            next: (result) => {
                this.response.set(result as unknown as Record<string, unknown>);
                this.loading.set(false);
            },
            error: (error) => {
                this.response.set(error?.error ?? { error: error?.message ?? 'Request failed' });
                this.loading.set(false);
            }
        });
    }

    queueUpload() {
        if (!this.airlineCode.trim() || !this.queueFile) return;

        this.loading.set(true);
        this.service.upload(this.queueFile, this.airlineCode.trim(), this.messageType).subscribe({
            next: (result) => {
                this.response.set(result as unknown as Record<string, unknown>);
                this.loading.set(false);
            },
            error: (error) => {
                this.response.set(error?.error ?? { error: error?.message ?? 'Upload failed' });
                this.loading.set(false);
            }
        });
    }

    getValidationSeverity(valid?: boolean): 'success' | 'danger' | 'info' {
        if (valid === true) return 'success';
        if (valid === false) return 'danger';
        return 'info';
    }

    formatResponse() {
        return JSON.stringify(this.response(), null, 2);
    }
}
