import { Injectable, inject } from '@angular/core';
import { ConfirmationService } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class CsmConfirmService {

    private confirmService = inject(ConfirmationService);

    // =========================
    // Generic Confirm
    // =========================
    confirm(message: string, accept: () => void) {
        this.confirmService.confirm({
            message,
            header: 'Confirmation',
            icon: 'pi pi-question-circle',
            acceptLabel: 'Yes',
            rejectLabel: 'No',
            accept
        });
    }

    // =========================
    // Delete Confirm (most used)
    // =========================
    delete(message: string, accept: () => void) {
        this.confirmService.confirm({
            message,
            header: 'Confirm Deletion',
            icon: 'pi pi-exclamation-triangle',
            acceptLabel: 'Delete',
            rejectLabel: 'Cancel',
            acceptButtonStyleClass: 'p-button-danger',
            rejectButtonStyleClass: 'p-button-text',
            accept
        });
    }
}
