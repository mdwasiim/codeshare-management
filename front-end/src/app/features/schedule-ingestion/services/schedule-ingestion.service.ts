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
    ScheduleMessageType,
    ScheduleValidationResponse,
    UploadResponse
} from '@features/schedule-ingestion/models/schedule-ingestion.model';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ScheduleIngestionService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    upload(file: File, airlineCode: string, expectedType?: ScheduleMessageType) {
        const body = new FormData();
        body.append('file', file);
        body.append('airlineCode', airlineCode);
        if (expectedType) {
            body.append('expectedType', expectedType);
        }

        return this.api.post<UploadResponse>(API_ENDPOINTS.scheduleIngestion.upload, body).pipe(tap(() => this.toast.success('Schedule file queued for processing')));
    }

    submitMessage(action: ScheduleAction, type: ScheduleMessageType, airlineCode: string, fileName: string, content: string) {
        const endpoint = API_ENDPOINTS.scheduleIngestion.messages[action];
        const body = { airlineCode, fileName, content };
        const request = this.api.post<ScheduleValidationResponse | ScheduleIngestionResponse>(endpoint, body, {
            pathParams: { type: type.toLowerCase() }
        });

        return request.pipe(tap(() => this.toast.success(`Schedule ${action} completed`)));
    }

    submitFile(action: ScheduleAction, type: ScheduleMessageType, airlineCode: string, file: File) {
        const endpoint = API_ENDPOINTS.scheduleIngestion.messages[action];
        const body = new FormData();
        body.append('file', file);
        body.append('airlineCode', airlineCode);

        return this.api.post<ScheduleValidationResponse | ScheduleIngestionResponse>(endpoint, body, {
            pathParams: { type: type.toLowerCase() }
        });
    }

    searchFiles(type: ScheduleMessageType, params: Record<string, string | number | boolean> = {}) {
        if (type === 'SSIM') {
            return this.api.get<PageResponse<ScheduleFileMetaData>>(API_ENDPOINTS.scheduleIngestion.ssim.files, { params });
        }

        return this.api.get<PageResponse<ScheduleFileMetaData>>(API_ENDPOINTS.scheduleIngestion.asmSsm.files, {
            pathParams: { type: type.toLowerCase() },
            params
        });
    }

    searchLoadedSchedules(type: ScheduleMessageType, params: Record<string, string | number | boolean> = {}) {
        if (type === 'SSIM') {
            return this.api.get<PageResponse<LoadedScheduleSummary>>(API_ENDPOINTS.scheduleIngestion.ssim.loadedSchedules, { params });
        }

        return this.api.get<PageResponse<LoadedScheduleSummary>>(API_ENDPOINTS.scheduleIngestion.asmSsm.loadedSchedules, {
            params: { ...params, type }
        });
    }

    getLoadedScheduleDetail(type: ScheduleMessageType, fileId: string) {
        if (type === 'SSIM') {
            return this.api.get<LoadedScheduleDetail>(API_ENDPOINTS.scheduleIngestion.ssim.loadedScheduleById, {
                pathParams: { fileId }
            });
        }

        return this.api.get<LoadedScheduleDetail>(API_ENDPOINTS.scheduleIngestion.asmSsm.loadedScheduleById, {
            pathParams: { fileId },
            params: { type }
        });
    }

    getFileSchedule(type: ScheduleMessageType, fileId: string) {
        if (type === 'SSIM') {
            return this.api.get<unknown>(API_ENDPOINTS.scheduleIngestion.ssim.messageByFileId, {
                pathParams: { fileId }
            });
        }

        return this.api.get<unknown>(API_ENDPOINTS.scheduleIngestion.asmSsm.scheduleByFileId, {
            pathParams: { type: type.toLowerCase(), fileId }
        });
    }

    searchFlights(type: ScheduleMessageType, fileId: string, params: Record<string, string | number | boolean> = {}) {
        if (type === 'SSIM') {
            return this.api.get<PageResponse<AnyScheduleFlight>>(API_ENDPOINTS.scheduleIngestion.ssim.fileFlights, {
                pathParams: { fileId },
                params
            });
        }

        return this.api.get<PageResponse<AnyScheduleFlight>>(API_ENDPOINTS.scheduleIngestion.asmSsm.fileFlights, {
            pathParams: { type: type.toLowerCase(), fileId },
            params
        });
    }
}
