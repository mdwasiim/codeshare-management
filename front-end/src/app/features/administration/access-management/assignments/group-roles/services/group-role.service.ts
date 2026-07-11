import { inject, Injectable } from '@angular/core';

import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { Role } from '@features/administration/access-management/models/role.model';
import { GroupService } from '@features/administration/access-management/identity/groups/services/group.service';
import { RoleService } from '@features/administration/access-management/identity/roles/services/role.service';
import { GroupRoleModel } from '@features/administration/access-management/assignments/group-roles/models/group-role.model';

@Injectable({
    providedIn: 'root'
})
export class GroupRoleService {
    private api = inject(AppApiService);
    private groupService = inject(GroupService);
    private roleService = inject(RoleService);

    // =====================================================
    // ROLES By Group
    // =====================================================
    getRolesByGroup(groupId: string) {
        return this.api.get<Role[]>(API_ENDPOINTS.accessManagement.groupRole.byGroupId, {
            pathParams: { groupId }
        });
    }

    getAllGroups() {
        return this.groupService.getAll();
    }

    getAllRoles() {
        return this.roleService.getAll();
    }
    replaceGroupRoles(groupId: string, roleId: string[]) {
        return this.api.put<GroupRoleModel[]>(API_ENDPOINTS.accessManagement.groupRole.byGroupId, roleId, {
            pathParams: {
                groupId
            }
        });
    }
}

