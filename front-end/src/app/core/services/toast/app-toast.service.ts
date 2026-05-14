// core/services/app-toast.service.ts
import { Injectable, inject } from '@angular/core';
import { MessageService } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class AppToastService {
    private messageService = inject(MessageService);

    private show(severity: 'success' | 'error' | 'warn' | 'info', detail?: string, summary?: string, life = 3000, sticky = false) {
        if (!detail?.trim()) return;

        this.messageService.add({
            severity,
            summary: summary || this.getDefaultSummary(severity),
            detail,
            life,
            sticky
        });
    }

    private getDefaultSummary(severity: string) {
        switch (severity) {
            case 'success':
                return 'Success';
            case 'error':
                return 'Error';
            case 'warn':
                return 'Warning';
            case 'info':
                return 'Info';
            default:
                return '';
        }
    }

    // ✅ consistent API everywhere
    success(detail: string, summary?: string, life?: number) {
        this.show('success', detail, summary, life);
    }

    error(detail: string, summary?: string, life = 5000) {
        this.show('error', detail, summary, life);
    }

    warn(detail: string, summary?: string, life?: number) {
        this.show('warn', detail, summary, life);
    }

    info(detail: string, summary?: string, life?: number) {
        this.show('info', detail, summary, life);
    }

    errorSticky(detail: string, summary?: string) {
        this.show('error', detail, summary, 0, true);
    }

    clear() {
        this.messageService.clear();
    }
}
