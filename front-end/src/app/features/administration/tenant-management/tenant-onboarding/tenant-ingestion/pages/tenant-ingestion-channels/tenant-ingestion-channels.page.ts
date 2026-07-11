import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'tenant-ingestion-channels',
    standalone: true,
    imports: [CommonModule],
    template: `
        <div class="access-page access-page--list">
            <div class="access-page-header">
                <div>
                    <span class="access-eyebrow">Ingestion</span>
                    <h1>Ingestion Channels</h1>
                    <p>Channels are managed inside each ingestion profile.</p>
                </div>
            </div>
            <div class="card p-4">
                <p>Open <strong>Ingestion Profiles</strong> to view and configure SFTP, MQ, email, and file channels.</p>
            </div>
        </div>
    `
})
export class TenantIngestionChannelsPage {}
