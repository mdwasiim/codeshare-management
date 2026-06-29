import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'app-dialog',
    standalone: true,
    imports: [CommonModule, DialogModule, ButtonModule],
    templateUrl: './app-dialog.component.html',
    styleUrl: './app-dialog.component.scss'
})
export class AppDialogComponent {
    @Input() visible: boolean = false;
    @Output() visibleChange = new EventEmitter<boolean>();

    @Input() title: string = '';
    @Input() subtitle: string = '';
    @Input() width: string = 'min(92vw, 640px)';
    @Input() closable: boolean = true;
    @Input() dismissableMask: boolean = false;

    @Input() disableSave: boolean = false;
    @Input() loading: boolean = false;
    @Input() saveLabel: string = 'Save';
    @Input() cancelLabel: string = 'Cancel';

    @Output() save = new EventEmitter<void>();
    @Output() cancel = new EventEmitter<void>();

    onCancel(): void {
        this.visible = false;
        this.visibleChange.emit(false);
        this.cancel.emit();
    }
    onSave(): void {
        if (!this.disableSave && !this.loading) {
            this.save.emit();
        }
    }

    onHide(): void {
        this.visible = false;
        this.visibleChange.emit(false);
    }

    onVisibleChange(value: boolean): void {
        this.visible = value;
        this.visibleChange.emit(value);
    }
}
