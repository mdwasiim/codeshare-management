import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'tenant-partner-profiles',
    standalone: true,
    imports: [CommonModule],
    template: `
        <div class="access-page access-page--list">
            <div class="access-page-header">
                <div>
                    <span class="access-eyebrow">Partner Management</span>
                    <h1>Partner Profiles</h1>
                    <p>Detailed partner profiles will be available here.</p>
                </div>
            </div>
            <div class="card p-4">
                <p>This screen is reserved for tenant-level partner profile configuration.</p>
            </div>
        </div>
    `
})
export class TenantPartnerProfilesPage {}
