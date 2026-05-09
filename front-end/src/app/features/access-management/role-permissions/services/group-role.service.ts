import {inject, Injectable} from '@angular/core';

import {AppApiService} from '@core/config/app-api.service';
import {Role} from "@features/iam/models/role.model";


@Injectable({
    providedIn: 'root'
})
export class GroupRoleService {

    private api = inject(AppApiService);

    // =====================================================
    // ROLES By Group
    // =====================================================
    getRolesByGroup(groupId: string) {
        return this.api.get<Role[]>('accessManagement.groupRole.roleByGroupId', {
            pathParams: { groupId }
        });
    }

}
