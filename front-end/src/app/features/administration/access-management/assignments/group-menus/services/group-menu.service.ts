import { inject, Injectable } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { GroupService } from '@features/administration/access-management/identity/groups/services/group.service';
import { MenuManagementService } from '@features/administration/access-management/authorization/menus/services/menu-management.service';
import { AppMenuModel } from '@features/administration/access-management/models/app-menu.model';
import { GroupMenuModel } from '@features/administration/access-management/assignments/group-menus/models/group-menu.model';

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
        return this.api.get<AppMenuModel[]>(API_ENDPOINTS.identityService.groupMenus.byGroupId, {
            pathParams: {
                groupId
            }
        });
    }

    // =====================================================
    // REPLACE GROUP MENUS
    // =====================================================
    replaceGroupMenus(groupId: string, menuIds: string[]) {
        return this.api.put<GroupMenuModel[]>(API_ENDPOINTS.identityService.groupMenus.byGroupId, menuIds, {
            pathParams: {
                groupId
            }
        });
    }
}

