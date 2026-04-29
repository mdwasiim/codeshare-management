import { Injectable } from '@angular/core';
import { of } from 'rxjs';
import {Organization} from "@features/settings/model/organization.model";

@Injectable({ providedIn: 'root' })
export class OrganizationService {

    private data: Organization[] = [
        { id: '1', name: 'Qatar Airways', code: 'QR', status: 'ACTIVE' },
        { id: '2', name: 'Emirates', code: 'EK', status: 'ACTIVE' }
    ];

    getAll() {
        return of(this.data);   // ✅ simulate API
    }

    getById(id: string) {
        return of(this.data.find(o => o.id === id));
    }

    create(org: Organization) {
        org.id = Date.now().toString();
        this.data.push(org);
        return of(org);
    }

    update(id: string, org: Organization) {
        const index = this.data.findIndex(o => o.id === id);
        this.data[index] = { ...org, id };
        return of(org);
    }

    delete(id: string) {
        this.data = this.data.filter(o => o.id !== id);
        return of(null);
    }
}
