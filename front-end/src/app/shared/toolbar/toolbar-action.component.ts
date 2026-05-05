import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToolbarModule } from 'primeng/toolbar';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';

import { CSMCrudPermissions } from '@core/models/app-permission.model';
import {FormsModule} from "@angular/forms";

type CrudAction = 'create' | 'delete' | 'export' | 'refresh' | 'upload' | 'search';

@Component({
    selector: 'csm-crud-toolbar',
    standalone: true,
    imports: [CommonModule, ToolbarModule, ButtonModule, InputTextModule,FormsModule],
    templateUrl: './toolbar-action.component.html'
})
export class ToolbarActionComponent {

    @Input() actions: CrudAction[] = [];

    /*@Input() permissions: CSMCrudPermissions = {
        canCreate: true,
        canDelete: true,
        canExport: true,
        canUpload: true,
        canRefresh: true
    };*/

    @Input() permissions?: Partial<CSMCrudPermissions>;

    @Input() hasSelection = false;
    @Input() searchText = '';

    @Output() create = new EventEmitter<void>();
    @Output() delete = new EventEmitter<void>();
    @Output() export = new EventEmitter<void>();
    @Output() refresh = new EventEmitter<void>();
    @Output() uploadFile = new EventEmitter<void>();
    @Output() search = new EventEmitter<string>();

    canShow(action: CrudAction): boolean {
        if (!this.permissions) return true;

        switch (action) {
            case 'create': return this.permissions.canCreate ?? false;
            case 'delete': return this.permissions.canDelete ?? false;
            case 'export': return this.permissions.canExport ?? false;
            case 'upload': return this.permissions.canUpload ?? true;
            case 'refresh': return this.permissions.canRefresh ?? true;
            default: return true;
        }
    }

    get hasAnyAction(): boolean {
        return this.visibleActions.length > 0;
    }

    isDeleteDisabled(): boolean {
        if (!this.permissions) return !this.hasSelection;
        return !(this.permissions.canDelete ?? false) || !this.hasSelection;
    }

    get visibleActions(): CrudAction[] {
        return this.actions.filter(a => this.canShow(a));
    }

    private get perms(): CSMCrudPermissions {
        return {
            canCreate: this.permissions?.canCreate ?? false,
            canDelete: this.permissions?.canDelete ?? false,
            canExport: this.permissions?.canExport ?? false,
            canUpload: this.permissions?.canUpload ?? false,
            canRefresh: this.permissions?.canRefresh ?? false
        };
    }
}
