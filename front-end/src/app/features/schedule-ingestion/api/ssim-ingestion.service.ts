import { inject, Injectable } from '@angular/core';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppApiService } from '@core/api/config/app-api.service';
import {
    AnyScheduleFlight,
    LoadedScheduleDetail,
    LoadedScheduleSummary,
    PageResponse,
    ScheduleFileMetaData,
    SsimValidationReportRow
} from '@features/schedule-ingestion/models/schedule-ingestion.model';

@Injectable({ providedIn: 'root' })
export class SsimIngestionService {
    private api = inject(AppApiService);

    searchFiles(params: Record<string, string | number | boolean> = {}) {
        return this.api.get<PageResponse<ScheduleFileMetaData>>(API_ENDPOINTS.scheduleIngestion.ssim.files, { params });
    }

    searchLoadedSchedules(params: Record<string, string | number | boolean> = {}) {
        return this.api.get<PageResponse<LoadedScheduleSummary>>(API_ENDPOINTS.scheduleIngestion.ssim.loadedSchedules, { params });
    }

    getLoadedScheduleDetail(fileId: string) {
        return this.api.get<LoadedScheduleDetail>(API_ENDPOINTS.scheduleIngestion.ssim.loadedScheduleById, {
            pathParams: { fileId }
        });
    }

    getFileSchedule(fileId: string) {
        return this.api.get<unknown>(API_ENDPOINTS.scheduleIngestion.ssim.messageByFileId, {
            pathParams: { fileId }
        });
    }

    getValidationReport(fileId: string) {
        return this.api.get<SsimValidationReportRow[]>(API_ENDPOINTS.scheduleIngestion.ssim.validationReport, {
            pathParams: { fileId }
        });
    }

    searchFlights(fileId: string, params: Record<string, string | number | boolean> = {}) {
        return this.api.get<PageResponse<AnyScheduleFlight>>(API_ENDPOINTS.scheduleIngestion.ssim.fileFlights, {
            pathParams: { fileId },
            params
        });
    }
}
