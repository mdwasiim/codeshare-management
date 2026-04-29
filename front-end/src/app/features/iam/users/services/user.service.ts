import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/config/app-api.service';
import { User } from '@features/iam/models/user.model';

@Injectable({ providedIn: 'root' })
export class UserService {

    private api = inject(AppApiService);

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
