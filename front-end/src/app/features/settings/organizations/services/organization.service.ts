import { Injectable, inject } from '@angular/core';
import { of, throwError } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Organization } from '@features/settings/model/organization.model';
import { AppToastService } from '@core/services/app-toast.service';

@Injectable({ providedIn: 'root' })
export class OrganizationService {

    private toast = inject(AppToastService);

    private data: Organization[] = [
        { id: '1', name: 'Qatar Airways', code: 'QR', status: 'ACTIVE' },
        { id: '2', name: 'Emirates', code: 'EK', status: 'ACTIVE' }
    ];

    // -----------------------------
    // GET ALL
    // -----------------------------
    getAll() {
        return of([...this.data]); // return copy
    }

    // -----------------------------
    // GET BY ID
    // -----------------------------
    getById(id: string) {
        const found = this.data.find(o => o.id === id);

        if (!found) {
            return throwError(() => new Error('Organization not found'));
        }

        return of({ ...found });
    }

    // -----------------------------
    // CREATE
    // -----------------------------
    create(org: Organization) {
        const newOrg = {
            ...org,
            id: Date.now().toString()
        };

        this.data.push(newOrg);

        return of(newOrg).pipe(
            tap(() => {
                this.toast.success('Organization created successfully');
            })
        );
    }

    // -----------------------------
    // UPDATE
    // -----------------------------
    update(id: string, org: Organization) {
        const index = this.data.findIndex(o => o.id === id);

        if (index === -1) {
            return throwError(() => new Error('Organization not found'));
        }

        this.data[index] = { ...org, id };

        return of({ ...this.data[index] }).pipe(
            tap(() => {
                this.toast.success('Organization updated successfully');
            })
        );
    }

    // -----------------------------
    // DELETE
    // -----------------------------
    delete(id: string) {
        const exists = this.data.some(o => o.id === id);

        if (!exists) {
            return throwError(() => new Error('Organization not found'));
        }

        this.data = this.data.filter(o => o.id !== id);

        return of(null).pipe(
            tap(() => {
                this.toast.success('Organization deleted successfully');
            })
        );
    }
}
