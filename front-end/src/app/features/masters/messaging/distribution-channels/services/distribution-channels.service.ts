import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { DistributionChannel } from '@features/masters/messaging/distribution-channels/models/distribution-channels.model';

@Injectable({ providedIn: 'root' })
export class DistributionChannelService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/distribution-channels';

    getAll(params?: Record<string, string>) { return this.api.get<DistributionChannel[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<DistributionChannel>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: DistributionChannel) {
        return this.api.post<DistributionChannel>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Distribution Channels created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Distribution Channels'); return throwError(() => err); })
        );
    }
    update(id: string, payload: DistributionChannel) {
        return this.api.put<DistributionChannel>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Distribution Channels updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Distribution Channels'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Distribution Channels deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Distribution Channels'); return throwError(() => err); })
        );
    }
}
