import { inject, Injectable } from '@angular/core';

import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';

import { RolePermissionModel } from '@features/administration/access-management/assignments/role-permissions/models/role-permission.model';

import { PermissionApiService } from '@features/administration/access-management/authorization/permissions/services/permission-api.service';
import { GroupService } from '@features/administration/access-management/identity/groups/services/group.service';

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
    getPermissionsByRole(roleId: string) {
        return this.api.get<RolePermissionModel[]>(API_ENDPOINTS.identityService.rolePermissions.byRoleId, {
            pathParams: { roleId }
        });
    }

    // =====================================================
    // REPLACE ROLE PERMISSIONS
    // =====================================================
    replaceRolePermissions(roleId: string, permissionIds: string[]) {
        return this.api.put<RolePermissionModel[]>(API_ENDPOINTS.identityService.rolePermissions.byRoleId, permissionIds, {
            pathParams: {
                roleId
            }
        });
    }
}

