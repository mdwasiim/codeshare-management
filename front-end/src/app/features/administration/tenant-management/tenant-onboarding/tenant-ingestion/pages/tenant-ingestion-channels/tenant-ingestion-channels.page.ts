import { Component, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { map } from 'rxjs';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';

import { BaseListComponent } from '@shared/components/base/base-list.component';
import { ToolbarActionComponent } from '@shared/components/toolbar/toolbar-action.component';

import { TenantIngestionChannel, TenantIngestionProfile } from '@features/administration/tenant-management/models/tenant-ingestion-profile.model';
import { TenantIngestionProfileService } from '../../services/tenant-ingestion-profile.service';

interface TenantIngestionChannelRow {
    id: string;
    tenantCode?: string;
    airlineCode?: string;
    sourceSystem: string;
    messageType?: string;
    sourceType?: string;
    protocol?: string;
    host?: string;
    port?: number;
    remoteDirectory?: string;
    brokerUrl?: string;
    queueName?: string;
    topicName?: string;
    fileIncludePattern?: string;
    priority?: number;
    enabled?: boolean;
    profileEnabled?: boolean;
}

@Component({
    selector: 'tenant-ingestion-channels',
    standalone: true,
    imports: [CommonModule, TableModule, ButtonModule, TagModule, ToolbarActionComponent],
    templateUrl: './tenant-ingestion-channels.page.html'
})
export class TenantIngestionChannelsPage extends BaseListComponent<TenantIngestionChannelRow> {
    protected override resourceName = 'TENANT';

    private readonly service = inject(TenantIngestionProfileService);

    @ViewChild('dt') private dt?: Table;

    override fetch() {
        return this.service.getAll().pipe(map((profiles) => this.flattenChannels(profiles ?? [])));
    }

    onSearch(value: string): void {
        this.dt?.filterGlobal(value, 'contains');
    }

    exportCsv(): void {
        this.dt?.exportCSV();
    }

    endpointSummary(channel: TenantIngestionChannelRow): string {
        if (channel.brokerUrl) {
            const destination = channel.queueName || channel.topicName;
            return destination ? `${channel.brokerUrl} / ${destination}` : channel.brokerUrl;
        }

        if (channel.host) {
            const remoteDirectory = channel.remoteDirectory ? ` / ${channel.remoteDirectory}` : '';
            const port = channel.port ? `:${channel.port}` : '';
            return `${channel.host}${port}${remoteDirectory}`;
        }

        return channel.fileIncludePattern || '-';
    }

    private flattenChannels(profiles: TenantIngestionProfile[]): TenantIngestionChannelRow[] {
        return profiles.flatMap((profile) =>
            (profile.channels ?? []).map((channel) => this.toChannelRow(profile, channel))
        );
    }

    private toChannelRow(profile: TenantIngestionProfile, channel: TenantIngestionChannel): TenantIngestionChannelRow {
        return {
            id:
                channel.id ||
                [
                    profile.id || profile.tenantCode || 'tenant',
                    channel.messageType || 'message',
                    channel.sourceType || 'source',
                    channel.host || channel.brokerUrl || channel.remoteDirectory || channel.fileIncludePattern || 'channel'
                ].join('-'),
            tenantCode: profile.tenantCode,
            airlineCode: profile.airlineCode,
            sourceSystem: profile.sourceSystem,
            messageType: channel.messageType,
            sourceType: channel.sourceType,
            protocol: channel.protocol,
            host: channel.host,
            port: channel.port,
            remoteDirectory: channel.remoteDirectory,
            brokerUrl: channel.brokerUrl,
            queueName: channel.queueName,
            topicName: channel.topicName,
            fileIncludePattern: channel.fileIncludePattern,
            priority: channel.priority,
            enabled: channel.enabled,
            profileEnabled: profile.enabled
        };
    }
}
