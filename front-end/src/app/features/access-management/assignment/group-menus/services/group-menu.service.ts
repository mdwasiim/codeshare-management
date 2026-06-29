import { inject, Injectable } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { GroupService } from '@features/access-management/iam/groups/services/group.service';
import { MenuManagementService } from '@features/access-management/iam/menus/services/menu-management.service';
import { AppMenuModel } from '@features/access-management/iam/models/app-menu.model';
import { GroupMenuModel } from '@features/access-management/assignment/group-menus/models/group-menu.model';

@Injectable({
    providedIn: 'root'
})
export class GroupMenuService {
    private api = inject(AppApiService);

    private groupService = inject(GroupService);

    private menuService = inject(MenuManagementService);

    // =====================================================
    // ALL GROUPS
    // =====================================================
    getAllGroups() {
        return this.groupService.getAll();
    }

    // =====================================================
    // ALL MENUS
    // =====================================================
    getAllMenus() {
        return this.menuService.getAll();
    }

    // =====================================================
    // MENUS BY GROUP
    // =====================================================
    getMenusByGroup(groupId: string) {
        return this.api.get<AppMenuModel[]>(API_ENDPOINTS.accessManagement.groupMenu.byGroupId, {
            pathParams: {
                groupId
            }
        });
    }

    // =====================================================
    // REPLACE GROUP MENUS
    // =====================================================
    replaceGroupMenus(groupId: string, menuIds: string[]) {
        return this.api.put<GroupMenuModel[]>(API_ENDPOINTS.accessManagement.groupMenu.byGroupId, menuIds, {
            pathParams: {
                groupId
            }
        });
    }
}
