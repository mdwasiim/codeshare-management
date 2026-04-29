import { Injectable, inject } from '@angular/core';
import { User } from '../models/user.model';
import {CSMApiService} from "@core/config/csm-api.service";

@Injectable({ providedIn: 'root' })
export class UserService {

    private api = inject(CSMApiService);

    getAll() {
        return this.api.get<User[]>('users.base');
    }

    getById(id: string) {
        return this.api.get<User>('users.byId', {
            pathParams: { id }
        });
    }

    create(user: User) {
        return this.api.post<User>('users.base', user);
    }

    update(id: string, user: User) {
        return this.api.put<User>('users.byId', user, {
            pathParams: { id }
        });
    }

    delete(id: string) {
        return this.api.delete<void>('users.byId', {
            pathParams: { id }
        });
    }
}
