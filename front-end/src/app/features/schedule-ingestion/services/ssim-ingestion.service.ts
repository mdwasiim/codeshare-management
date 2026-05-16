import { inject, Injectable } from '@angular/core';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import {
    AnyScheduleFlight,
    LoadedScheduleDetail,
    LoadedScheduleSummary,
    PageResponse,
    ScheduleAction,
    ScheduleFileMetaData,
    ScheduleIngestionResponse,
    ScheduleValidationResponse,
    UploadResponse
} from '@features/schedule-ingestion/models/schedule-ingestion.model';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class SsimIngestionService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    upload(file: File, airlineCode: string) {
        const body = new FormData();
        body.append('file', file);
        body.append('airlineCode', airlineCode);
        body.append('expectedType', 'SSIM');

        return this.api.post<UploadResponse>(API_ENDPOINTS.scheduleIngestion.upload, body).pipe(tap(() => this.toast.success('SSIM file queued for processing')));
    }

    submitFile(action: ScheduleAction, airlineCode: string, file: File) {
        const endpoint = API_ENDPOINTS.scheduleIngestion.messages[action];
        const body = new FormData();
        body.append('file', file);
        body.append('airlineCode', airlineCode);

        return this.api.post<ScheduleValidationResponse | ScheduleIngestionResponse>(endpoint, body, {
            pathParams: { type: 'ssim' }
        });
    }

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

    searchFlights(fileId: string, params: Record<string, string | number | boolean> = {}) {
        return this.api.get<PageResponse<AnyScheduleFlight>>(API_ENDPOINTS.scheduleIngestion.ssim.fileFlights, {
            pathParams: { fileId },
            params
        });
    }
}
