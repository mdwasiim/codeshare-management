import {Component, inject, OnInit, ViewChild} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import {Table, TableModule} from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';

import { BaseListComponent } from '@core/base/base-list.component';
import {Organization} from "@features/settings/model/organization.model";
import {of} from "rxjs";

// 🔸 replace with real service later
class MockOrgService {

    getAll() {
        return of([
            { id: '1', name: 'Qatar Airways', code: 'QR', status: 'ACTIVE' },
            { id: '2', name: 'Emirates', code: 'EK', status: 'ACTIVE' }
        ]);
    }

    delete(id: string) {
        return {
            subscribe: ({ next }: any) => next()
        };
    }
}

@Component({
    selector: 'organization-list',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        TagModule,
        InputTextModule
    ],
    templateUrl: './organization-list.page.html'
})
export class OrganizationListPage extends BaseListComponent<Organization> implements OnInit{

    @ViewChild('dt') dt!: Table;
    private router = inject(Router);
    private service = new MockOrgService(); // 🔥 replace with real service

    ngOnInit(): void {
        this.loadData();   // 🔥 MUST CALL HERE
    }
    // 🔥 required by BaseList
    fetch() {
        return this.service.getAll();
    }

    create() {
        this.router.navigate(['/settings/organizations/create']);
    }

    edit(org: any) {
        this.router.navigate(['/settings/organizations', org.id]);
    }

    delete(org: any) {
        if (!confirm(`Delete "${org.name}"?`)) return;

        this.service.delete(org.id).subscribe(() => this.refresh());
    }

    onSearch(event: Event) {
        const input = event.target as HTMLInputElement;
        this.dt.filterGlobal(input.value, 'contains');
    }
}
