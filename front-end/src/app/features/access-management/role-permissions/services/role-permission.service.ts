import {
    inject,
    Injectable
} from '@angular/core';

import {
    AppApiService
} from '@core/config/app-api.service';

import {
    RolePermissionModel
} from '@features/access-management/role-permissions/models/role-permission.model';

import {
    RoleService
} from '@features/iam/roles/services/role.service';
import {PermissionApiService} from "@features/iam/permissions/services/permission-api.service";
import {GroupService} from "@features/iam/groups/services/group.service";


@Injectable({
    providedIn: 'root'
})
export class RolePermissionService {

    private api = inject(AppApiService);

    private roleService = inject(RoleService);

    private permissionApiService = inject(PermissionApiService);

    private groupRoleService = inject(GroupService);

    // =====================================================
    // ROLES
    // =====================================================
    getGroups() {

        return this.groupRoleService.getAll();
    }

    // =====================================================
    // ALL PERMISSIONS
    // =====================================================
    getPermissions() {

        return this.permissionApiService.getAll();
    }

    // =====================================================
    // ASSIGNED ROLE PERMISSIONS
    // =====================================================
    getPermissionsByRole(
        roleId: string
    ) {
        return this.api.get<RolePermissionModel[]>(
            'accessManagement.rolePermissions.byRoleId',{
                pathParams: { roleId }
            });
    }

    // =====================================================
    // REPLACE ROLE PERMISSIONS
    // =====================================================
    replaceRolePermissions(
        roleId: string,
        permissionIds: string[]
    ) {

        return this.api.put<RolePermissionModel[]>(
            'accessManagement.rolePermissions.byRoleId',
            permissionIds,
            {
                pathParams: {
                    roleId
                }
            }
        );
    }
}
