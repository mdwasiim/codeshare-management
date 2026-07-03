import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ScheduleMessageType } from '@features/schedule-ingestion/models/schedule-ingestion.model';

interface ChangeMetric {
    label: string;
    value: string;
}

@Component({
    selector: 'schedule-comparison',
    standalone: true,
    imports: [CommonModule, RouterLink, ButtonModule, TagModule],
    templateUrl: './schedule-comparison.page.html',
    styleUrls: ['./schedule-comparison.page.scss']
})
export class ScheduleComparisonPage {
    private route = inject(ActivatedRoute);

    readonly type = (this.route.snapshot.paramMap.get('type') || 'SSIM') as ScheduleMessageType;
    readonly fileId = this.route.snapshot.paramMap.get('fileId') || '';
    readonly fileName = this.route.snapshot.queryParamMap.get('fileName') || '-';
    readonly airlineCode = this.route.snapshot.queryParamMap.get('airlineCode') || '-';

    readonly metrics: ChangeMetric[] = [
        { label: 'Added flights', value: '-' },
        { label: 'Cancelled flights', value: '-' },
        { label: 'Updated timings', value: '-' },
        { label: 'Changed routing', value: '-' },
        { label: 'Changed DEI information', value: '-' }
    ];

    get backRoute() {
        return this.type === 'SSIM' ? '/ssim-ingestion' : '/asm-ssm-ingestion';
    }
}
