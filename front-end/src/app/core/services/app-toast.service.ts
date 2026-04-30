// core/services/app-toast.service.ts
import { Injectable, inject } from '@angular/core';
import { MessageService } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class AppToastService {

    private messageService = inject(MessageService);

    private show(
        severity: 'success' | 'error' | 'warn' | 'info',
        detail?: string,
        summary?: string
    ) {
        if (!detail?.trim()) return; // ✅ clean + TS safe

        this.messageService.add({
            severity,
            summary: summary || this.getDefaultSummary(severity),
            detail
        });
    }

    private getDefaultSummary(severity: string) {
        switch (severity) {
            case 'success': return 'Success';
            case 'error': return 'Error';
            case 'warn': return 'Warning';
            case 'info': return 'Info';
            default: return '';
        }
    }

    success(detail?: string, summary?: string) {
        this.show('success', detail, summary);
    }

    error(detail?: string, summary?: string) {
        this.show('error', detail, summary);
    }

    warn(detail?: string, summary?: string) {
        this.show('warn', detail, summary);
    }

    info(detail?: string, summary?: string) {
        this.show('info', detail, summary);
    }
}
