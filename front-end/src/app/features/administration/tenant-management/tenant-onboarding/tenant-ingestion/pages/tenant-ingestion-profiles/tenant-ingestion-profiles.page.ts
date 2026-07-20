import { Component, Input, OnChanges, OnInit, SimpleChanges, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TagModule } from 'primeng/tag';
import { TextareaModule } from 'primeng/textarea';
import { TooltipModule } from 'primeng/tooltip';

import { AuthTenantService } from '@services/auth/auth-tenant.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { TenantIngestionChannel, TenantIngestionProfile } from '@features/administration/tenant-management/models/tenant-ingestion-profile.model';
import { TenantIngestionProfileService } from '../../services/tenant-ingestion-profile.service';
import { MasterReferenceLookupService } from '@features/masters/shared/master-reference-lookup.service';

type ChannelForm = FormGroup;

@Component({
    selector: 'tenant-ingestion-profiles',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, ButtonModule, CheckboxModule, InputTextModule, SelectModule, TagModule, TextareaModule, TooltipModule],
    templateUrl: './tenant-ingestion-profiles.page.html',
    styleUrl: './tenant-ingestion-profiles.page.scss'
})
export class TenantIngestionProfilesPage implements OnChanges, OnInit {
    private readonly fb = inject(FormBuilder);
    private readonly service = inject(TenantIngestionProfileService);
    private readonly toast = inject(AppToastService);
    private readonly authTenant = inject(AuthTenantService);
    private readonly masterLookup = inject(MasterReferenceLookupService);

    loading = false;
    saving = false;

    @Input() adminMode = false;
    @Input() tenantContext: { id?: number; tenantCode?: string } | null = null;

    sourceTypeOptions: Array<{ label: string; value: string }> = [];
    messageTypeOptions: Array<{ label: string; value: string }> = [];

    form = this.fb.group({
        id: [null as number | null],
        tenantId: [null as number | null],
        tenantCode: [''],
        airlineCode: [''],
        sourceSystem: ['OAG', [Validators.required, Validators.maxLength(50)]],
        enabled: [false],
        pollIntervalMs: [300000],
        channels: this.fb.array<ChannelForm>([])
    });

    get tenantCode(): string {
        return this.form.get('tenantCode')?.value || this.authTenant.getTenantCode() || '-';
    }

    get channels(): FormArray<ChannelForm> {
        return this.form.get('channels') as FormArray<ChannelForm>;
    }

    get enabledChannelsCount(): number {
        return this.channels.controls.filter((channel) => !!channel.get('enabled')?.value).length;
    }

    ngOnInit(): void {
        this.loadReferenceOptions();
        this.load();
    }

    private loadReferenceOptions(): void {
        this.masterLookup.getReferenceOptions('INGESTION_SOURCE_TYPE').subscribe((options) => {
            this.sourceTypeOptions = options;
        });
        this.masterLookup.getReferenceOptions('INGESTION_MESSAGE_TYPE').subscribe((options) => {
            this.messageTypeOptions = options;
        });
    }

    ngOnChanges(changes: SimpleChanges): void {
        if ((changes['tenantContext'] || changes['adminMode']) && !changes['tenantContext']?.firstChange) {
            this.load();
        }
    }

    load(): void {
        if (this.adminMode) {
            this.loadAdminTenantProfile();
            return;
        }

        this.loading = true;
        this.service.getCurrent().subscribe({
            next: (profile) => {
                this.patchProfile(profile);
                this.loading = false;
            },
            error: () => {
                this.loading = false;
                this.toast.error('Failed to load ingestion setup');
            }
        });
    }

    save(): void {
        if (this.form.invalid || this.saving) {
            this.form.markAllAsTouched();
            return;
        }

        this.saving = true;
        const payload = this.toPayload();
        const request = this.adminMode
            ? payload.id
                ? this.service.update(payload.id, payload)
                : this.service.create(payload)
            : this.service.saveCurrent(payload);

        request.subscribe({
            next: (profile) => {
                this.patchProfile(profile);
                this.saving = false;
                this.toast.success('Ingestion setup saved');
            },
            error: () => {
                this.saving = false;
                this.toast.error('Failed to save ingestion setup');
            }
        });
    }

    addChannel(): void {
        this.channels.push(
            this.createChannelGroup({
                messageType: 'SSIM',
                sourceType: 'SFTP',
                enabled: true,
                priority: this.channels.length + 1,
                port: 22,
                protocol: 'SFTP'
            })
        );
    }

    removeChannel(index: number): void {
        this.channels.removeAt(index);
        this.resequencePriorities();
    }

    channelSourceType(index: number): string {
        return this.channels.at(index)?.get('sourceType')?.value || '';
    }

    endpointSummary(channel: ChannelForm): string {
        const value = channel.value as TenantIngestionChannel;

        if (value.sourceType === 'MQ') {
            return value.queueName || value.topicName || value.brokerUrl || 'Queue or topic not set';
        }

        if (value.sourceType === 'EMAIL') {
            return value.mailbox || value.protocol || 'Mailbox not set';
        }

        if (value.sourceType === 'LOCAL' || value.sourceType === 'CLOUD') {
            return value.remoteDirectory || value.fileIncludePattern || 'Location not set';
        }

        if (value.host) {
            const port = value.port ? `:${value.port}` : '';
            return `${value.host}${port}`;
        }

        return 'Endpoint not set';
    }

    sourceLabel(sourceType: string): string {
        return this.sourceTypeOptions.find((option) => option.value === sourceType)?.label || sourceType || 'Channel';
    }

    private patchProfile(profile: TenantIngestionProfile): void {
        this.channels.clear();
        (profile.channels ?? []).forEach((channel) => this.channels.push(this.createChannelGroup(channel)));

        this.form.patchValue({
            id: profile.id ?? null,
            tenantId: profile.tenantId ?? null,
            tenantCode: profile.tenantCode ?? '',
            airlineCode: profile.airlineCode ?? profile.tenantCode ?? '',
            sourceSystem: profile.sourceSystem || 'OAG',
            enabled: !!profile.enabled,
            pollIntervalMs: profile.pollIntervalMs ?? 300000
        });

        this.form.markAsPristine();
        this.form.markAsUntouched();
    }

    private loadAdminTenantProfile(): void {
        if (!this.tenantContext?.id || !this.tenantContext?.tenantCode) {
            this.patchProfile(this.toDraftProfile());
            return;
        }

        this.loading = true;
        this.service.getAll().subscribe({
            next: (profiles) => {
                const profile = profiles.find((item) => item.tenantId === this.tenantContext?.id || item.tenantCode === this.tenantContext?.tenantCode) ?? this.toDraftProfile();
                this.patchProfile(profile);
                this.loading = false;
            },
            error: () => {
                this.loading = false;
                this.toast.error('Failed to load ingestion setup');
            }
        });
    }

    private toDraftProfile(): TenantIngestionProfile {
        return {
            tenantId: this.tenantContext?.id,
            tenantCode: this.tenantContext?.tenantCode,
            airlineCode: this.tenantContext?.tenantCode,
            sourceSystem: 'OAG',
            enabled: false,
            pollIntervalMs: 300000,
            channels: []
        };
    }

    private createChannelGroup(channel: TenantIngestionChannel = {}): ChannelForm {
        return this.fb.group({
            id: [channel.id ?? null],
            messageType: [channel.messageType ?? 'SSIM', Validators.required],
            sourceType: [channel.sourceType ?? 'SFTP', Validators.required],
            partnerCode: [channel.partnerCode ?? ''],
            enabled: [channel.enabled ?? true],
            priority: [channel.priority ?? this.channels.length + 1],
            host: [channel.host ?? ''],
            port: [channel.port ?? null],
            username: [channel.username ?? ''],
            remoteDirectory: [channel.remoteDirectory ?? ''],
            protocol: [channel.protocol ?? ''],
            mailbox: [channel.mailbox ?? ''],
            brokerUrl: [channel.brokerUrl ?? ''],
            queueName: [channel.queueName ?? ''],
            topicName: [channel.topicName ?? ''],
            fileIncludePattern: [channel.fileIncludePattern ?? '']
        });
    }

    private toPayload(): TenantIngestionProfile {
        const raw = this.form.getRawValue();

        return {
            id: raw.id ?? undefined,
            tenantId: raw.tenantId ?? undefined,
            tenantCode: raw.tenantCode || undefined,
            airlineCode: raw.airlineCode || raw.tenantCode || undefined,
            sourceSystem: raw.sourceSystem?.trim() || 'OAG',
            enabled: !!raw.enabled,
            pollIntervalMs: this.toNumber(raw.pollIntervalMs),
            channels: (raw.channels ?? []).map((channel) => this.toChannelPayload(channel as TenantIngestionChannel))
        };
    }

    private toChannelPayload(channel: TenantIngestionChannel): TenantIngestionChannel {
        return {
            ...channel,
            partnerCode: channel.partnerCode?.trim() || undefined,
            host: channel.host?.trim() || undefined,
            username: channel.username?.trim() || undefined,
            remoteDirectory: channel.remoteDirectory?.trim() || undefined,
            protocol: channel.protocol?.trim() || undefined,
            mailbox: channel.mailbox?.trim() || undefined,
            brokerUrl: channel.brokerUrl?.trim() || undefined,
            queueName: channel.queueName?.trim() || undefined,
            topicName: channel.topicName?.trim() || undefined,
            fileIncludePattern: channel.fileIncludePattern?.trim() || undefined,
            port: this.toNumber(channel.port),
            priority: this.toNumber(channel.priority),
            enabled: !!channel.enabled
        };
    }

    private resequencePriorities(): void {
        this.channels.controls.forEach((channel, index) => {
            if (!channel.get('priority')?.value) {
                channel.get('priority')?.setValue(index + 1);
            }
        });
    }

    private toNumber(value: unknown): number | undefined {
        if (value === null || value === undefined || value === '') {
            return undefined;
        }
        const parsed = Number(value);
        return Number.isFinite(parsed) ? parsed : undefined;
    }
}
