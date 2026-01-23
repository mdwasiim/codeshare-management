import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'organization-list',
    standalone: true,
    imports: [CommonModule, TableModule, ButtonModule],
    template: `
        <div class="card">
            <h2>Organizations</h2>

            <p-table [value]="organizations" dataKey="id">
                <ng-template pTemplate="header">
                    <tr>
                        <th>Name</th>
                        <th>Code</th>
                        <th>Status</th>
                        <th style="width: 120px">Action</th>
                    </tr>
                </ng-template>

                <ng-template pTemplate="body" let-org>
                    <tr>
                        <td>{{ org.name }}</td>
                        <td>{{ org.code }}</td>
                        <td>{{ org.status }}</td>
                        <td>
                            <button
                                pButton
                                label="View"
                                icon="pi pi-eye"
                                (click)="view(org.id)">
                            </button>
                        </td>
                    </tr>
                </ng-template>
            </p-table>
        </div>
    `
})
export class OrganizationList {

    organizations = [
        { id: '1', name: 'Qatar Airways', code: 'QR', status: 'ACTIVE' },
        { id: '2', name: 'Emirates', code: 'EK', status: 'ACTIVE' }
    ];

    constructor(private router: Router) {}

    view(id: string) {
        this.router.navigate(['/organizations', id]);
    }
}
