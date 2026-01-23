import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class OrganizationService {

    private baseUrl = '/api/tenants'; // backend remains tenant

    constructor(private http: HttpClient) {}

    getAll(): Observable<any[]> {
        return this.http.get<any[]>(this.baseUrl);
    }

    getById(id: string): Observable<any> {
        return this.http.get<any>(`${this.baseUrl}/${id}`);
    }
}
