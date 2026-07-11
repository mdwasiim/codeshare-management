import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';
import { User } from '@features/administration/access-management/models/user.model';
import { tap, catchError, throwError } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    getAll() {
        return this.api.get<User[]>(API_ENDPOINTS.accessManagement.users.base);
    }

    getById(id: string) {
        return this.api.get<User>(API_ENDPOINTS.accessManagement.users.byId, {
            pathParams: { id }
        });
    }

    create(user: User) {
        return this.api.post<User>(API_ENDPOINTS.accessManagement.users.base, user).pipe(
            tap(() => {
                this.toast.success('User created successfully');
            }),
            catchError((err) => {
                this.toast.error(err.message || 'Failed to create user');
                return throwError(() => err);
            })
        );
    }

    update(id: string, user: User) {
        return this.api
            .put<User>(API_ENDPOINTS.accessManagement.users.byId, user, {
                pathParams: { id }
            })
            .pipe(
                tap(() => {
                    this.toast.success('User updated successfully');
                }),
                catchError((err) => {
                    this.toast.error(err.message || 'Failed to update user');
                    return throwError(() => err);
                })
            );
    }

    delete(id: string) {
        return this.api
            .delete<void>(API_ENDPOINTS.accessManagement.users.byId, {
                pathParams: { id }
            })
            .pipe(
                tap(() => {
                    this.toast.success('User deleted successfully');
                }),
                catchError((err) => {
                    this.toast.error(err.message || 'Failed to delete user');
                    return throwError(() => err);
                })
            );
    }
}

