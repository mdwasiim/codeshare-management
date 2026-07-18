import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';

import {
    ReferenceDataCompletenessIssue,
    ReferenceDataCompletenessResponse
} from '@features/masters/validation/reference-data-completeness/models/reference-data-completeness.model';
import { ReferenceDataCompletenessService } from '@features/masters/validation/reference-data-completeness/services/reference-data-completeness.service';

@Component({
    selector: 'reference-data-completeness',
    standalone: true,
    imports: [CommonModule, ButtonModule, TableModule, TagModule, TooltipModule],
    templateUrl: './reference-data-completeness.page.html',
    styleUrl: './reference-data-completeness.page.scss'
})
export class ReferenceDataCompletenessPage implements OnInit {
    private service = inject(ReferenceDataCompletenessService);

    loading = false;
    response: ReferenceDataCompletenessResponse | null = null;

    ngOnInit(): void {
        this.refresh();
    }

    refresh(): void {
        this.loading = true;
        this.service.getOutboundScheduleReadiness().subscribe({
            next: (response) => {
                this.response = response ?? { complete: false, issues: [] };
                this.loading = false;
            },
            error: () => {
                this.response = null;
                this.loading = false;
            }
        });
    }

    get issues(): ReferenceDataCompletenessIssue[] {
        return this.response?.issues ?? [];
    }

    get isComplete(): boolean {
        return this.response?.complete === true;
    }

    get issueCount(): number {
        return this.issues.length;
    }

    get missingCodeCount(): number {
        return this.issues.filter((issue) => !!issue.code).length;
    }

    get emptyCategoryCount(): number {
        return this.issues.filter((issue) => !issue.code).length;
    }
}
