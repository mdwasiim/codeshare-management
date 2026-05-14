import { Component, EventEmitter, Input, Output } from '@angular/core';

import { CommonModule } from '@angular/common';

import { FormsModule } from '@angular/forms';

import { ToolbarModule } from 'primeng/toolbar';

import { ButtonModule } from 'primeng/button';

import { InputTextModule } from 'primeng/inputtext';
import { HasPermissionDirective } from '@shared/directives/permission/has-permission.directive';

export type CrudAction = 'create' | 'delete' | 'export' | 'refresh' | 'upload' | 'search' | 'reset' | 'save';

@Component({
    selector: 'csm-crud-toolbar',
    standalone: true,
    imports: [CommonModule, FormsModule, ToolbarModule, ButtonModule, InputTextModule, HasPermissionDirective],
    templateUrl: './toolbar-action.component.html'
})
export class ToolbarActionComponent {
    // =========================
    // INPUTS
    // =========================
    @Input()
    actions: CrudAction[] = [];

    @Input()
    resource?: string;

    @Input()
    hasSelection = false;

    @Input()
    searchText = '';

    @Input()
    loading = false;

    // =========================
    // OUTPUTS
    // =========================
    @Output()
    create = new EventEmitter<void>();

    @Output()
    delete = new EventEmitter<void>();

    @Output()
    export = new EventEmitter<void>();

    @Output()
    refresh = new EventEmitter<void>();

    @Output()
    uploadFile = new EventEmitter<void>();

    @Output()
    search = new EventEmitter<string>();

    @Output()
    reset = new EventEmitter<void>();

    @Output()
    save = new EventEmitter<void>();

    // =========================
    // HELPERS
    // =========================
    get hasAnyAction(): boolean {
        return this.actions.length > 0;
    }

    hasAction(action: CrudAction): boolean {
        return this.actions.includes(action);
    }

    permission(action: string): string {
        if (!this.resource) {
            return '';
        }

        return `${this.resource}:${action}`.toLowerCase();
    }

    isDeleteDisabled(): boolean {
        return !this.hasSelection;
    }
}
