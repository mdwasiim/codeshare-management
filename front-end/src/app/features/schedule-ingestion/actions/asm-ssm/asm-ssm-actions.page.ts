import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { TextareaModule } from 'primeng/textarea';
import { ScheduleAction, ScheduleMessageType, ScheduleValidationMessage } from '@features/schedule-ingestion/models/schedule-ingestion.model';
import { AsmSsmIngestionService } from '@features/schedule-ingestion/api/asm-ssm-ingestion.service';
import { MasterLookupOption, MasterReferenceLookupService } from '@features/masters/shared/master-reference-lookup.service';

@Component({
    selector: 'asm-ssm-actions',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink, ButtonModule, InputTextModule, SelectModule, TableModule, TextareaModule],
    templateUrl: './asm-ssm-actions.page.html',
    styleUrls: ['./asm-ssm-actions.page.scss']
})
export class AsmSsmActionsPage implements OnInit {
    private service = inject(AsmSsmIngestionService);
    private lookupService = inject(MasterReferenceLookupService);

    readonly messageTypes: ScheduleMessageType[] = ['ASM', 'SSM'];
    readonly actions: { label: string; value: ScheduleAction }[] = [
        { label: 'Validate', value: 'validate' },
        { label: 'Parse', value: 'parse' },
        { label: 'Ingest', value: 'ingest' }
    ];

    type: ScheduleMessageType = 'ASM';
    airlineCode = '';
    action: ScheduleAction = 'validate';
    messageFileName = 'schedule-message.txt';
    content = '';
    messageFile: File | null = null;
    queueFile: File | null = null;
    result: Record<string, unknown> | null = null;
    loading = false;
    airlineOptions: MasterLookupOption[] = [];

    ngOnInit(): void {
        this.lookupService.getOptions('airlineCode').subscribe((options) => {
            this.airlineOptions = options;
        });
    }

    get validationMessages(): ScheduleValidationMessage[] {
        if (!this.result || !('messages' in this.result)) return [];
        return (this.result['messages'] as ScheduleValidationMessage[] | undefined) ?? [];
    }

    onMessageFileSelected(event: Event) {
        const input = event.target as HTMLInputElement;
        this.messageFile = input.files?.[0] ?? null;
        if (this.messageFile) {
            this.messageFileName = this.messageFile.name;
        }
    }

    onQueueFileSelected(event: Event) {
        const input = event.target as HTMLInputElement;
        this.queueFile = input.files?.[0] ?? null;
    }

    runAction() {
        if (!this.airlineCode.trim() || (!this.messageFile && !this.content.trim())) return;

        this.loading = true;
        const request = this.messageFile
            ? this.service.submitFile(this.action, this.type, this.airlineCode.trim(), this.messageFile)
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
        this.service.upload(this.type, this.queueFile, this.airlineCode.trim()).subscribe({
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
