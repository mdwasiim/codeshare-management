import {inject, Injectable} from '@angular/core';

import {AppApiService} from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';

import {UserService} from '@features/access-management/iam/users/services/user.service';

import {GroupService} from '@features/access-management/iam/groups/services/group.service';

import {Group} from '@features/access-management/iam/models/group.model';

import {UserGroupModel} from '../models/user-group.model';

@Injectable({
    providedIn: 'root'
})
export class UserGroupService {

    private api = inject(AppApiService);

    private userService =
        inject(UserService);

    private groupService =
        inject(GroupService);

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
    getGroupsByUser(
        userId: string
    ) {

        return this.api.get<Group[]>(
            API_ENDPOINTS.accessManagement.userGroups.byUserId,
            {
                pathParams: {
                    userId
                }
            }
        );
    }

    // =====================================================
    // REPLACE USER GROUPS
    // =====================================================
    replaceUserGroups(
        userId: string,
        groupIds: string[]
    ) {

        return this.api.put<UserGroupModel[]>(
            API_ENDPOINTS.accessManagement.userGroups.byUserId,
            groupIds,
            {
                pathParams: {
                    userId
                }
            }
        );
    }
}
