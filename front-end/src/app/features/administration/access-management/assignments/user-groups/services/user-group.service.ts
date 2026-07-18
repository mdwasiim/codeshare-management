import { inject, Injectable } from '@angular/core';

import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';

import { UserService } from '@features/administration/access-management/identity/users/services/user.service';

import { GroupService } from '@features/administration/access-management/identity/groups/services/group.service';

import { Group } from '@features/administration/access-management/models/group.model';
import { User } from '@features/administration/access-management/models/user.model';

import { UserGroupModel } from '../models/user-group.model';

@Injectable({
    providedIn: 'root'
})
export class UserGroupService {
    private api = inject(AppApiService);

    private userService = inject(UserService);

    private groupService = inject(GroupService);

    // =====================================================
    // ALL USERS
    // =====================================================
    getAllUsers() {
        return this.userService.getAll();
    }

    // =====================================================
    // ALL GROUPS
    // =====================================================
    getAllGroups() {
        return this.groupService.getAll();
    }

    // =====================================================
    // GROUPS BY USER
    // =====================================================
    getGroupsByUser(userId: string) {
        return this.api.get<Group[]>(API_ENDPOINTS.identityService.userGroups.byUserId, {
            pathParams: {
                userId
            }
        });
    }

    // =====================================================
    // USERS BY GROUP
    // =====================================================
    getUsersByGroup(groupId: string) {
        return this.api.get<User[]>(API_ENDPOINTS.identityService.userGroups.byGroupId, {
            pathParams: {
                groupId
            }
        });
    }

    // =====================================================
    // REPLACE USER GROUPS
    // =====================================================
    replaceUserGroups(userId: string, groupIds: string[]) {
        return this.api.put<UserGroupModel[]>(API_ENDPOINTS.identityService.userGroups.byUserId, groupIds, {
            pathParams: {
                userId
            }
        });
    }

    // =====================================================
    // REPLACE GROUP USERS
    // =====================================================
    replaceGroupUsers(groupId: string, userIds: string[]) {
        return this.api.put<UserGroupModel[]>(API_ENDPOINTS.identityService.userGroups.byGroupId, userIds, {
            pathParams: {
                groupId
            }
        });
    }
}

