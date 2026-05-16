import { Injectable } from '@angular/core';

import { AuthTokenService } from '@services/auth/auth-token.service';

import { PermissionService } from '@core/security/permission.service';
import { AuthTenantService } from '@services/auth/auth-tenant.service';

@Injectable({
    providedIn: 'root'
})
export class AuthInitializerService {
    constructor(
        private tenantService: AuthTenantService,
        private tokenService: AuthTokenService,
        private permissionService: PermissionService
    ) {}

    init(): void {
        const tenantCode = this.tokenService.tenant;

        if (tenantCode) {
            this.tenantService.setTenant('', tenantCode);
        }
        this.permissionService.setGroups(this.tokenService.groups || []);

        this.permissionService.setPermissions(this.tokenService.permissions || []);

        this.permissionService.setRoles(this.tokenService.roles || []);
    }
}
