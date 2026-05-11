import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/config/app-api.service';
import { AppToastService } from '@services/app-toast.service';
import { Group } from '@features/access-management/iam/models/group.model';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class GroupService {

    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    // -----------------------------
    // GET ALL
    // -----------------------------
    getAll() {
        return this.api.get<Group[]>('accessManagement.groups.base');
    }

    // -----------------------------
    // GET BY ID
    // -----------------------------
    getById(id: string) {
        return this.api.get<Group>('accessManagement.groups.byId', {
            pathParams: { id }
        });
    }

    // -----------------------------
    // CREATE
    // -----------------------------
    create(group: Group) {
        return this.api.post<Group>('accessManagement.groups.base', group).pipe(
            tap(() => {
                this.toast.success('Group created successfully');
            })
        );
    }

    // -----------------------------
    // UPDATE
    // -----------------------------
    update(id: string, group: Group) {
        return this.api.put<Group>('accessManagement.groups.byId', group, {
            pathParams: { id }
        }).pipe(
            tap(() => {
                this.toast.success('Group updated successfully');
            })
        );
    }

    // -----------------------------
    // DELETE
    // -----------------------------
    delete(id: string) {
        return this.api.delete<void>('accessManagement.groups.byId', {
            pathParams: { id }
        }).pipe(
            tap(() => {
                this.toast.success('Group deleted successfully');
            })
        );
    }
}
