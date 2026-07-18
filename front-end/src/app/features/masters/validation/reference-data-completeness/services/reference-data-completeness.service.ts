import { Injectable, inject } from '@angular/core';

import { AppApiService } from '@core/api/config/app-api.service';
import { ReferenceDataCompletenessResponse } from '@features/masters/validation/reference-data-completeness/models/reference-data-completeness.model';

@Injectable({ providedIn: 'root' })
export class ReferenceDataCompletenessService {
    private api = inject(AppApiService);
    private readonly outboundScheduleUrl = '/master/internal/reference-data-completeness/outbound-schedule';

    getOutboundScheduleReadiness() {
        return this.api.get<ReferenceDataCompletenessResponse>(this.outboundScheduleUrl);
    }
}
