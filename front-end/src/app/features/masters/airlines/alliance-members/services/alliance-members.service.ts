import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AllianceMember } from '@features/masters/airlines/alliance-members/models/alliance-members.model';

@Injectable({ providedIn: 'root' })
export class AllianceMemberService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/alliance-members';

    getAll() { return this.api.get<AllianceMember[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<AllianceMember>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: AllianceMember) {
        return this.api.post<AllianceMember>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Alliance Members created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Alliance Members'); return throwError(() => err); })
        );
    }
    update(id: string, payload: AllianceMember) {
        return this.api.put<AllianceMember>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Alliance Members updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Alliance Members'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Alliance Members deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Alliance Members'); return throwError(() => err); })
        );
    }
}
