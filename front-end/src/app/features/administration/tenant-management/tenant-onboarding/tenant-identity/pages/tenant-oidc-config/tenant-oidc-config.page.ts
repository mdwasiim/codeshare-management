import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'tenant-oidc-config',
    standalone: true,
    imports: [CommonModule],
    template: `
        <div class="access-page access-page--list">
            <div class="access-page-header">
                <div>
                    <span class="access-eyebrow">Identity</span>
                    <h1>OIDC Configuration</h1>
                    <p>OIDC settings are maintained within each tenant record.</p>
                </div>
            </div>
            <div class="card p-4">
                <p>Navigate to <strong>Tenants</strong>, select a tenant, and update the <em>Identity Configuration</em> section to manage OIDC parameters.</p>
            </div>
        </div>
    `
})
export class TenantOidcConfigPage {}
