import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class AuthTenantService {
    private tenantId: string | null = null;

    private tenantCode: string | null = null;

    // =========================
    // SET TENANT
    // =========================
    setTenant(tenantId: string, tenantCode: string): void {
        this.tenantId = tenantId;
        this.tenantCode = tenantCode;
    }

    // =========================
    // CLEAR
    // =========================
    clear(): void {
        this.tenantId = null;
        this.tenantCode = null;
    }

    // =========================
    // GETTERS
    // =========================
    getTenantId(): string | null {
        return this.tenantId;
    }

    getTenantCode(): string | null {
        return this.tenantCode;
    }

    hasTenant(): boolean {
        return !!this.tenantCode;
    }
}
