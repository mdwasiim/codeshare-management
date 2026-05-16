import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { ScheduleAction, ScheduleValidationMessage } from '@features/schedule-ingestion/models/schedule-ingestion.model';
import { SsimIngestionService } from '@features/schedule-ingestion/services/ssim-ingestion.service';

@Component({
    selector: 'ssim-actions',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink, ButtonModule, InputTextModule, SelectModule, TableModule],
    templateUrl: './ssim-actions.page.html',
    styleUrls: ['./ssim-actions.page.scss']
})
export class SsimActionsPage {
    private service = inject(SsimIngestionService);

    readonly actions: { label: string; value: ScheduleAction }[] = [
        { label: 'Validate', value: 'validate' },
        { label: 'Parse', value: 'parse' },
        { label: 'Ingest', value: 'ingest' }
    ];

    airlineCode = '';
    action: ScheduleAction = 'validate';
    actionFile: File | null = null;
    queueFile: File | null = null;
    result: Record<string, unknown> | null = null;
    loading = false;

    get validationMessages(): ScheduleValidationMessage[] {
        if (!this.result || !('messages' in this.result)) return [];
        return (this.result['messages'] as ScheduleValidationMessage[] | undefined) ?? [];
    }

    onActionFileSelected(event: Event) {
        const input = event.target as HTMLInputElement;
        this.actionFile = input.files?.[0] ?? null;
    }

    onQueueFileSelected(event: Event) {
        const input = event.target as HTMLInputElement;
        this.queueFile = input.files?.[0] ?? null;
    }

    runAction() {
        if (!this.airlineCode.trim() || !this.actionFile) return;

        this.loading = true;
        this.service.submitFile(this.action, this.airlineCode.trim(), this.actionFile).subscribe({
            next: (response) => {
                this.result = response as unknown as Record<string, unknown>;
                this.loading = false;
            },
            error: (error) => {
                this.result = error?.error ?? { error: error?.message ?? 'Request failed' };
                this.loading = false;
            }
        });
    }

    queueUpload() {
        if (!this.airlineCode.trim() || !this.queueFile) return;

        this.loading = true;
        this.service.upload(this.queueFile, this.airlineCode.trim()).subscribe({
            next: (response) => {
                this.result = response as unknown as Record<string, unknown>;
                this.loading = false;
            },
            error: (error) => {
                this.result = error?.error ?? { error: error?.message ?? 'Upload failed' };
                this.loading = false;
            }
        });
    }

    formatResult() {
        return JSON.stringify(this.result, null, 2);
    }
}
