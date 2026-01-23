import { Component, EventEmitter, Input, Output } from '@angular/core';

import { ButtonModule } from 'primeng/button';
import { CSMCrudPermissions } from '@/core/models/csm-permission.model';
import { ToolbarModule } from 'primeng/toolbar';
import { CommonModule, JsonPipe } from '@angular/common';

@Component({
  selector: 'csm-crud-toolbar',
  standalone: true,
  imports: [ToolbarModule, ButtonModule, CommonModule],
  templateUrl: './csm-crud-toolbar.component.html'
})
export class CSMCrudToolbarComponent {

  @Input() permissions!: CSMCrudPermissions;

  ngOnInit() {
    // Apply defaults only if no permissions provided by parent
    if (!this.permissions) {
      this.permissions = {
        canCreate: true,
        canDelete: true,
        canExport: true
      };
    }
  }

  get hasAnyPermission(): boolean {
    return this.permissions.canCreate ||
          this.permissions.canDelete ||
          this.permissions.canExport;
  }

  @Input() disableDelete = false;

  @Output() create = new EventEmitter<void>();
  @Output() deleteSelected = new EventEmitter<void>();
  @Output() export = new EventEmitter<void>();
}
