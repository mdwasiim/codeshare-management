import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'tenant-identity-providers',
    standalone: true,
    imports: [CommonModule],
    template: `
        <div class="access-page access-page--list">
            <div class="access-page-header">
                <div>
                    <span class="access-eyebrow">Identity</span>
                    <h1>Identity Providers</h1>
                    <p>View tenant identity providers. Edit from the Tenant form.</p>
                </div>
            </div>
            <div class="card p-4">
                <p>Tenant identity provider management is configured per tenant. Select a tenant in <strong>Tenants</strong> to update its auth source and OIDC settings.</p>
            </div>
        </div>
    `
})
export class TenantIdentityProvidersPage {}
