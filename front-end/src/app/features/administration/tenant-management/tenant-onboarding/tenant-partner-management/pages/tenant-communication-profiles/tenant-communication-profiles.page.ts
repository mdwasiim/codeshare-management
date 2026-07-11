import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'tenant-communication-profiles',
    standalone: true,
    imports: [CommonModule],
    template: `
        <div class="access-page access-page--list">
            <div class="access-page-header">
                <div>
                    <span class="access-eyebrow">Partner Management</span>
                    <h1>Communication Profiles</h1>
                    <p>Communication profiles will be available here.</p>
                </div>
            </div>
            <div class="card p-4">
                <p>This screen is reserved for tenant-level communication profile configuration.</p>
            </div>
        </div>
    `
})
export class TenantCommunicationProfilesPage {}
