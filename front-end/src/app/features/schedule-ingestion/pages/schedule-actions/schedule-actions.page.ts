import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { TextareaModule } from 'primeng/textarea';
import { ScheduleAction, ScheduleMessageType, ScheduleValidationMessage } from '@features/schedule-ingestion/models/schedule-ingestion.model';
import { ScheduleIngestionService } from '@features/schedule-ingestion/services/schedule-ingestion.service';

@Component({
    selector: 'schedule-actions',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink, ButtonModule, InputTextModule, SelectModule, TableModule, TagModule, TextareaModule],
    templateUrl: './schedule-actions.page.html',
    styleUrls: ['./schedule-actions.page.scss']
})
export class ScheduleActionsPage implements OnInit {
    private route = inject(ActivatedRoute);
    private service = inject(ScheduleIngestionService);

    readonly asmSsmTypes: ScheduleMessageType[] = ['ASM', 'SSM'];
    readonly actions: { label: string; value: ScheduleAction }[] = [
        { label: 'Validate', value: 'validate' },
        { label: 'Parse', value: 'parse' },
        { label: 'Ingest', value: 'ingest' }
    ];

    mode: 'SSIM' | 'ASM_SSM' = 'SSIM';
    type: ScheduleMessageType = 'SSIM';
    airlineCode = '';
    action: ScheduleAction = 'validate';
    messageFileName = 'schedule-message.txt';
    content = '';
    selectedMessageFile: File | null = null;
    queueFile: File | null = null;
    result: Record<string, unknown> | null = null;
    loading = false;

    ngOnInit(): void {
        this.mode = this.route.snapshot.data['mode'] === 'ASM_SSM' ? 'ASM_SSM' : 'SSIM';
        this.type = this.mode === 'SSIM' ? 'SSIM' : 'ASM';
    }

    get title() {
        return this.mode === 'SSIM' ? 'SSIM Actions' : 'Schedule Actions (ASM/SSM)';
    }

    get listRoute() {
        return this.mode === 'SSIM' ? '/ssim-ingestion' : '/asm-ssm-ingestion';
    }

    get canUseTextPayload() {
        return this.mode === 'ASM_SSM';
    }

    get validationMessages(): ScheduleValidationMessage[] {
        if (!this.result || !('messages' in this.result)) return [];
        return (this.result['messages'] as ScheduleValidationMessage[] | undefined) ?? [];
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
        if (!this.airlineCode.trim() || (!this.selectedMessageFile && (!this.canUseTextPayload || !this.content.trim()))) return;

        this.loading = true;
        const request = this.selectedMessageFile
            ? this.service.submitFile(this.action, this.type, this.airlineCode.trim(), this.selectedMessageFile)
            : this.service.submitMessage(this.action, this.type, this.airlineCode.trim(), this.messageFileName.trim(), this.content);

        request.subscribe({
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
        this.service.upload(this.queueFile, this.airlineCode.trim(), this.type).subscribe({
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
