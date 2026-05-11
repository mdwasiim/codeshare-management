import {inject, Injectable} from '@angular/core';

import {AppApiService} from '@core/config/app-api.service';
import {Role} from "@features/access-management/iam/models/role.model";
import {GroupService} from "@features/access-management/iam/groups/services/group.service";
import {RoleService} from "@features/access-management/iam/roles/services/role.service";
import {RolePermissionModel} from "@features/access-management/assignment/role-permissions/models/role-permission.model";
import {GroupRoleModel} from "@features/access-management/assignment/group-roles/models/group-role.model";


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
        return this.api.get<Role[]>('accessManagement.groupRole.byGroupId', {
            pathParams: { groupId }
        });
    }

    getAllGroups() {
        return this.groupService.getAll();
    }

    getAllRoles() {
        return this.roleService.getAll();
    }
    replaceGroupRoles(groupId: string,
                      roleId: string[]
    ) {

        return this.api.put<GroupRoleModel []>(
            'accessManagement.groupRole.byGroupId',
            roleId,
            {
                pathParams: {
                    groupId
                }
            }
        );
    }

}
