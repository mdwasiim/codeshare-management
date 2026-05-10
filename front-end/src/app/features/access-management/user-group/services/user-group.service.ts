import {inject, Injectable} from '@angular/core';

import {AppApiService} from '@core/config/app-api.service';

import {UserService} from '@features/iam/users/services/user.service';

import {GroupService} from '@features/iam/groups/services/group.service';

import {Group} from '@features/iam/models/group.model';

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
            'accessManagement.userGroups.byUserId',
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
            'accessManagement.userGroups.byUserId',
            groupIds,
            {
                pathParams: {
                    userId
                }
            }
        );
    }
}
