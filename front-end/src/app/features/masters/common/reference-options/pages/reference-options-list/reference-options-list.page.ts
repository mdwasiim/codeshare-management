import { CommonModule } from '@angular/common';
import { Component, ViewChild, inject } from '@angular/core';
import { forkJoin } from 'rxjs';
import { ButtonModule } from 'primeng/button';
import { Table, TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';

import { BaseListComponent } from '@shared/components/base/base-list.component';
import { AppDialogComponent } from '@shared/components/app-dialog/app-dialog.component';
import { ToolbarActionComponent } from '@shared/components/toolbar/toolbar-action.component';
import { HasPermissionDirective } from '@shared/directives/permission/has-permission.directive';
import { AppConfirmService } from '@services/app-confirm.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { CommonReferenceOption } from '@features/masters/common/reference-options/models/reference-options.model';
import { CommonReferenceOptionService } from '@features/masters/common/reference-options/services/reference-options.service';
import { CommonReferenceOptionFormPage } from '@features/masters/common/reference-options/pages/reference-options-form/reference-options-form.page';

@Component({
    selector: 'common-reference-options-list',
    standalone: true,
    imports: [CommonModule, TableModule, ButtonModule, TagModule, TooltipModule, ToolbarActionComponent, AppDialogComponent, HasPermissionDirective, CommonReferenceOptionFormPage],
    templateUrl: './reference-options-list.page.html'
})
export class CommonReferenceOptionListPage extends BaseListComponent<CommonReferenceOption> {
    protected override resourceName = 'COMMONREFERENCEOPTION';

    dialogVisible = false;
    selectedId: string | null = null;
    selectedRecords: CommonReferenceOption[] = [];

    private service = inject(CommonReferenceOptionService);
    private toast = inject(AppToastService);
    private confirm = inject(AppConfirmService);

    @ViewChild('dt') dt!: Table;

    override fetch() {
        return this.service.getAll(this.exactFilters);
    }

    openCreate(): void {
        this.selectedId = null;
        this.dialogVisible = true;
    }

    openEdit(record: CommonReferenceOption): void {
        this.selectedId = record.id ?? null;
        this.dialogVisible = true;
    }

    deleteRecord(record: CommonReferenceOption): void {
        if (!record.id) return;

        this.confirm.delete('Delete ' + this.recordLabel(record) + '?', () => {
            this.service.delete(record.id!).subscribe({
                next: () => this.refresh(),
                error: () => this.toast.error('Failed to delete common reference option')
            });
        });
    }

    deleteSelectedRecords(): void {
        if (!this.selectedRecords.length) return;

        this.confirm.delete('Delete ' + this.selectedRecords.length + ' selected record(s)?', () => {
            const requests = this.selectedRecords
                .filter((record) => !!record.id)
                .map((record) => this.service.delete(record.id!));
            forkJoin(requests).subscribe({
                next: () => {
                    this.selectedRecords = [];
                    this.refresh();
                },
                error: () => this.toast.error('Failed to delete selected common reference options')
            });
        });
    }

    exportCSV(): void {
        this.dt.exportCSV();
    }

    onSaved(): void {
        this.dialogVisible = false;
        this.refresh();
    }

    onSearch(value: string): void {
        this.dt.filterGlobal(value, 'contains');
    }

    recordLabel(record: CommonReferenceOption): string {
        return [record.categoryCode, record.value].filter(Boolean).join(' / ') || record.label || record.id || 'record';
    }

    statusSeverity(status?: string): 'success' | 'secondary' | 'danger' | 'info' | 'warn' {
        switch (status) {
            case 'ACTIVE':
                return 'success';
            case 'INACTIVE':
                return 'secondary';
            case 'DRAFT':
                return 'info';
            case 'ARCHIVED':
                return 'warn';
            default:
                return 'secondary';
        }
    }
}
