import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/config/app-api.service';
import { Group } from '@features/iam/models/group.model';

@Injectable({ providedIn: 'root' })
export class GroupService {

    private api = inject(AppApiService);

    getAll(tenantId: string) {
        return this.api.get<Group[]>('groups.base', {
            params: { tenantId }   // ✅ correct
        });
    }

    getById(id: string) {
        return this.api.get<Group>('groups.byId', {
            pathParams: { id }
        });
    }

    create(group: Group) {
        return this.api.post<Group>('groups.base', group);
    }

    update(id: string, group: Group) {
        return this.api.put<Group>('groups.byId', group, {
            pathParams: { id }
        });
    }

    delete(id: string) {
        return this.api.delete<void>('groups.byId', {
            pathParams: { id }
        });
    }
}
