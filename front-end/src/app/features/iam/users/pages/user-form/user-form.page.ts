import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { UserService } from '@features/iam/users/services/user.service';
import { User } from '@features/iam/models/user.model';
import { BaseListComponent } from '@core/base/base-list.component';

@Component({
    selector: 'csm-user-list',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './user-list.component.html'
})
export class UserListComponent extends BaseListComponent<User> {

    private service = inject(UserService);
    private router = inject(Router);

    fetch() {
        return this.service.getAll();
    }

    createUser() {
        this.router.navigate(['/iam/users/create']);
    }

    editUser(user: User) {
        this.router.navigate(['/iam/users', user.id]);
    }

    deleteUser(user: User) {
        if (!confirm(`Delete user "${user.username}"?`)) return;

        this.service.delete(user.id!).subscribe(() => this.loadData());
    }
}
