import { inject, Injectable } from '@angular/core';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppApiService } from '@core/api/config/app-api.service';
import {
    AnyScheduleFlight,
    LoadedScheduleDetail,
    LoadedScheduleMessageSummary,
    LoadedScheduleSummary,
    OutboundScheduleMessage,
    PageResponse,
    ScheduleFileMetaData,
    ScheduleMessageType
} from '@features/schedule-ingestion/models/schedule-ingestion.model';

@Injectable({ providedIn: 'root' })
export class AsmSsmIngestionService {
    private api = inject(AppApiService);

    searchFiles(type: ScheduleMessageType, params: Record<string, string | number | boolean> = {}) {
        return this.api.get<PageResponse<ScheduleFileMetaData>>(API_ENDPOINTS.scheduleIngestionService.asmSsm.files, {
            pathParams: { type: type.toLowerCase() },
            params
        });
    }

    searchMessages(type: ScheduleMessageType, params: Record<string, string | number | boolean> = {}) {
        return this.api.get<PageResponse<LoadedScheduleMessageSummary>>(API_ENDPOINTS.scheduleIngestionService.asmSsm.messages, {
            pathParams: { type: type.toLowerCase() },
            params
        });
    }

    searchLoadedSchedules(type: ScheduleMessageType, params: Record<string, string | number | boolean> = {}) {
        return this.api.get<PageResponse<LoadedScheduleSummary>>(API_ENDPOINTS.scheduleIngestionService.asmSsm.loadedSchedules, {
            params: { ...params, type }
        });
    }

    getLoadedScheduleDetail(type: ScheduleMessageType, fileId: string) {
        return this.api.get<LoadedScheduleDetail>(API_ENDPOINTS.scheduleIngestionService.asmSsm.loadedScheduleById, {
            pathParams: { fileId },
            params: { type }
        });
    }

    getFileSchedule(type: ScheduleMessageType, fileId: string) {
        return this.api.get<unknown>(API_ENDPOINTS.scheduleIngestionService.asmSsm.scheduleByFileId, {
            pathParams: { type: type.toLowerCase(), fileId }
        });
    }

    searchFlights(type: ScheduleMessageType, fileId: string, params: Record<string, string | number | boolean> = {}) {
        return this.api.get<PageResponse<AnyScheduleFlight>>(API_ENDPOINTS.scheduleIngestionService.asmSsm.fileFlights, {
            pathParams: { type: type.toLowerCase(), fileId },
            params
        });
    }

    getOutboundMessage(outboundMessageId: string) {
        return this.api.get<OutboundScheduleMessage>(API_ENDPOINTS.scheduleIngestionService.outboundMessages.byId, {
            pathParams: { outboundMessageId }
        });
    }
}
