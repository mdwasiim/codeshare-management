import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { CodesharePartner } from '@features/masters/airlines/codeshare-partners/models/codeshare-partners.model';

@Injectable({ providedIn: 'root' })
export class CodesharePartnerService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/codeshare-partners';

    getAll() { return this.api.get<CodesharePartner[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<CodesharePartner>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: CodesharePartner) {
        return this.api.post<CodesharePartner>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Codeshare Partners created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Codeshare Partners'); return throwError(() => err); })
        );
    }
    update(id: string, payload: CodesharePartner) {
        return this.api.put<CodesharePartner>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Codeshare Partners updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Codeshare Partners'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Codeshare Partners deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Codeshare Partners'); return throwError(() => err); })
        );
    }
}
