import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'csm-dialog',
    standalone: true,
    imports: [
        CommonModule,
        DialogModule,
        ButtonModule
    ],
    templateUrl: './csm-dialog.component.html'
})
export class CsmDialogComponent {

    // =========================
    // Visibility Control
    // =========================
    @Input() visible: boolean = false;
    @Output() visibleChange = new EventEmitter<boolean>();

    // =========================
    // Dialog Config
    // =========================
    @Input() title: string = '';
    @Input() width: string = '520px';
    @Input() closable: boolean = true;

    // =========================
    // Actions
    // =========================
    @Input() disableSave: boolean = false;
    @Input() loading: boolean = false;

    @Output() save = new EventEmitter<void>();
    @Output() cancel = new EventEmitter<void>();

    // =========================
    // Methods
    // =========================

    onCancel(): void {
        this.visibleChange.emit(false);
        this.cancel.emit();
    }

    onSave(): void {
        if (!this.disableSave) {
            this.save.emit();
        }
    }

    onHide(): void {
        // Ensures sync if user clicks outside / close icon
        this.visibleChange.emit(false);
    }

}
