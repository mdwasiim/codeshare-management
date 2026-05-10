import {inject, Injectable} from "@angular/core";
import {AppApiService} from "@core/config/app-api.service";
import {GroupService} from "@features/iam/groups/services/group.service";
import {MenuManagementService} from "@features/iam/menus/services/menu-management.service";
import {AppMenuModel} from "@features/iam/models/app-menu.model";
import {GroupMenuModel} from "@features/access-management/group-menus/models/group-menu.model";

@Injectable({
    providedIn: 'root'
})
export class GroupMenuService {

    private api = inject(AppApiService);

    private groupService =
        inject(GroupService);

    private menuService =
        inject(MenuManagementService);

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
    getMenusByGroup(
        groupId: string
    ) {

        return this.api.get<AppMenuModel[]>(
            'accessManagement.groupMenu.byGroupId',
            {
                pathParams: {
                    groupId
                }
            }
        );
    }

    // =====================================================
    // REPLACE GROUP MENUS
    // =====================================================
    replaceGroupMenus(
        groupId: string,
        menuIds: string[]
    ) {

        return this.api.put<GroupMenuModel[]>(
            'accessManagement.groupMenu.byGroupId',
            menuIds,
            {
                pathParams: {
                    groupId
                }
            }
        );
    }
}
