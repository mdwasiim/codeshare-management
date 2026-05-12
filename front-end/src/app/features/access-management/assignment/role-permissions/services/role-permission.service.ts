import {
    inject,
    Injectable
} from '@angular/core';

import {
    AppApiService
} from '@core/config/app-api.service';
import { API_ENDPOINTS } from '@core/config/app-api.config';

import {
    RolePermissionModel
} from '@features/access-management/assignment/role-permissions/models/role-permission.model';

import {PermissionApiService} from "@features/access-management/iam/permissions/services/permission-api.service";
import {GroupService} from "@features/access-management/iam/groups/services/group.service";


@Injectable({
    providedIn: 'root'
})
export class RolePermissionService {

    private api = inject(AppApiService);

    private permissionApiService = inject(PermissionApiService);

    private groupService = inject(GroupService);

    // =====================================================
    // ROLES
    // =====================================================
    getGroups() {

        return this.groupService.getAll();
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
            API_ENDPOINTS.accessManagement.rolePermissions.byRoleId,{
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
            API_ENDPOINTS.accessManagement.rolePermissions.byRoleId,
            permissionIds,
            {
                pathParams: {
                    roleId
                }
            }
        );
    }
}
