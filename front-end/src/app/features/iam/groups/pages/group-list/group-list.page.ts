import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { GroupService } from '../../services/group.service';
import { Group } from '@features/iam/models/group.model';
import { BaseListComponent } from '@core/base/base-list.component';

@Component({
    selector: 'app-group-list',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './group-list.page.html'
})
export class GroupListPage extends BaseListComponent<Group> {

    private service = inject(GroupService);
    private router = inject(Router);

    tenantId = 'QR';

    fetch() {
        return this.service.getAll(this.tenantId);
    }

    createGroup() {
        this.router.navigate(['/iam/groups/create']);
    }

    editGroup(group: Group) {
        this.router.navigate(['/iam/groups', group.id]);
    }

    deleteGroup(group: Group) {
        if (!confirm(`Delete group "${group.name}"?`)) return;

        this.service.delete(group.id!).subscribe(() => this.loadData());
    }
}
