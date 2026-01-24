import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class TenantService {

  // 🔒 hardcoded for now
  private readonly TENANT_CODE = 'QR';

  getTenantCode(): string {
    return this.TENANT_CODE;
  }

  // later:
  // setTenant(code: string) { ... }
}
