import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { CheckboxModule } from 'primeng/checkbox';
import { AppDialogComponent } from '@shared/components/app-dialog/app-dialog.component';
import { AppFormSectionComponent } from '@shared/components/form-section/app-form-section.component';
import { TenantService } from '../../../services/tenant.service';
import { Tenant } from '@features/access-management/models/tenant.model';
import { TenantIngestionChannel, TenantIngestionProfile } from '../../../models/tenant-ingestion-profile.model';
import { TenantIngestionProfileService } from '../../../services/tenant-ingestion-profile.service';
import { AppToastService } from '@services/toast/app-toast.service';

@Component({
    selector: 'tenant-ingestion-tab',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        TableModule,
        ButtonModule,
        TagModule,
        SelectModule,
        InputTextModule,
        CheckboxModule,
        AppDialogComponent,
        AppFormSectionComponent
    ],
    templateUrl: './tenant-ingestion-tab.component.html'
})
export class TenantIngestionTabComponent {
    private readonly route = inject(ActivatedRoute);
    private readonly tenantService = inject(TenantService);
    private readonly ingestionService = inject(TenantIngestionProfileService);
    private readonly fb = inject(FormBuilder);
    private readonly toast = inject(AppToastService);

    tenant: Tenant | null = null;
    profile: TenantIngestionProfile | null = null;

    profileDialogVisible = false;
    channelDialogVisible = false;
    editingChannelIndex = -1;

    readonly sourceTypeOptions = ['FTP', 'SFTP', 'EMAIL', 'JMS', 'KAFKA', 'LOCAL_FILE'].map((value) => ({ label: value.replace('_', ' '), value }));
    readonly messageTypeOptions = ['SSIM', 'ASM', 'SSM'].map((value) => ({ label: value, value }));
    readonly protocolOptions = ['IMAPS', 'POP3S', 'FTPS', 'SFTP', 'FILE', 'JMS', 'KAFKA'].map((value) => ({ label: value, value }));

    readonly profileForm = this.fb.group({
        id: [''],
        tenantId: [''],
        tenantCode: [''],
        airlineCode: ['', Validators.required],
        sourceSystem: ['', Validators.required],
        enabled: [true],
        pollIntervalMs: [300000, Validators.required]
    });

    readonly channelForm = this.fb.group({
        id: [''],
        messageType: ['SSIM', Validators.required],
        sourceType: ['SFTP', Validators.required],
        enabled: [true],
        priority: [1, Validators.required],
        host: [''],
        port: [null as number | null],
        username: [''],
        passwordEncrypted: [''],
        remoteDirectory: [''],
        protocol: [''],
        mailbox: [''],
        brokerUrl: [''],
        queueName: [''],
        topicName: [''],
        fileIncludePattern: [''],
        fileExcludePattern: [''],
        filePollDelayMs: [60000],
        fileMove: [''],
        fileMoveFailed: [''],
        connectionTimeoutMs: [30000],
        readTimeoutMs: [30000],
        retryAttempts: [3],
        retryDelayMs: [1000]
    });

    constructor() {
        this.route.parent?.paramMap
            .pipe(
                switchMap((params) => {
                    const id = params.get('id');
                    return id ? this.tenantService.getById(id) : of(null);
                })
            )
            .subscribe((tenant) => {
                this.tenant = tenant;
                if (!tenant?.tenantCode) {
                    this.profile = null;
                    return;
                }
                this.loadProfile(tenant.tenantCode);
            });
    }

    loadProfile(tenantCode: string) {
        this.ingestionService.getByTenantCode(tenantCode).subscribe((profile) => {
            this.profile = profile;
        });
    }

    openProfileDialog() {
        this.profileForm.reset({
            id: this.profile?.id || '',
            tenantId: this.profile?.tenantId || this.tenant?.id || '',
            tenantCode: this.profile?.tenantCode || this.tenant?.tenantCode || '',
            airlineCode: this.profile?.airlineCode || this.tenant?.tenantCode || '',
            sourceSystem: this.profile?.sourceSystem || '',
            enabled: this.profile?.enabled ?? true,
            pollIntervalMs: this.profile?.pollIntervalMs ?? 300000
        });
        this.profileDialogVisible = true;
    }

    saveProfile() {
        if (this.profileForm.invalid || !this.tenant) {
            this.profileForm.markAllAsTouched();
            return;
        }

        const formValue = this.profileForm.getRawValue();
        const payload: TenantIngestionProfile = {
            ...this.profile,
            id: formValue.id || undefined,
            tenantId: formValue.tenantId || this.tenant.id,
            tenantCode: formValue.tenantCode || this.tenant.tenantCode,
            airlineCode: formValue.airlineCode || this.tenant.tenantCode,
            sourceSystem: formValue.sourceSystem || undefined,
            enabled: formValue.enabled ?? true,
            pollIntervalMs: formValue.pollIntervalMs ?? 300000,
            channels: this.profile?.channels || []
        };

        const request = payload.id ? this.ingestionService.update(payload.id, payload) : this.ingestionService.create(payload);
        request.subscribe((saved) => {
            this.profile = saved;
            this.profileDialogVisible = false;
        });
    }

    openChannelDialog(channel?: TenantIngestionChannel, index = -1) {
        if (!this.profile && !this.profileForm.get('sourceSystem')?.value) {
            this.toast.warn('Create the ingestion profile first');
            return;
        }

        this.editingChannelIndex = index;
        this.channelForm.reset({
            id: channel?.id || '',
            messageType: channel?.messageType || 'SSIM',
            sourceType: channel?.sourceType || 'SFTP',
            enabled: channel?.enabled ?? true,
            priority: channel?.priority ?? (this.profile?.channels?.length || 0) + 1,
            host: channel?.host || '',
            port: channel?.port ?? null,
            username: channel?.username || '',
            passwordEncrypted: channel?.passwordEncrypted || '',
            remoteDirectory: channel?.remoteDirectory || '',
            protocol: channel?.protocol || '',
            mailbox: channel?.mailbox || '',
            brokerUrl: channel?.brokerUrl || '',
            queueName: channel?.queueName || '',
            topicName: channel?.topicName || '',
            fileIncludePattern: channel?.fileIncludePattern || '',
            fileExcludePattern: channel?.fileExcludePattern || '',
            filePollDelayMs: channel?.filePollDelayMs ?? 60000,
            fileMove: channel?.fileMove || '',
            fileMoveFailed: channel?.fileMoveFailed || '',
            connectionTimeoutMs: channel?.connectionTimeoutMs ?? 30000,
            readTimeoutMs: channel?.readTimeoutMs ?? 30000,
            retryAttempts: channel?.retryAttempts ?? 3,
            retryDelayMs: channel?.retryDelayMs ?? 1000
        });
        this.channelDialogVisible = true;
    }

    saveChannel() {
        if (this.channelForm.invalid) {
            this.channelForm.markAllAsTouched();
            return;
        }

        const channel = {
            ...this.channelForm.getRawValue(),
            id: this.channelForm.getRawValue().id || undefined
        } as TenantIngestionChannel;
        const channels = [...(this.profile?.channels || [])];

        if (this.editingChannelIndex >= 0) {
            channels[this.editingChannelIndex] = channel;
        } else {
            channels.push(channel);
        }

        this.persistChannels(channels, () => {
            this.channelDialogVisible = false;
            this.editingChannelIndex = -1;
        });
    }

    deleteChannel(index: number) {
        const channels = [...(this.profile?.channels || [])];
        channels.splice(index, 1);
        this.persistChannels(channels);
    }

    persistChannels(channels: TenantIngestionChannel[], callback?: () => void) {
        if (!this.tenant) return;

        const formValue = this.profileForm.getRawValue();
        const payload: TenantIngestionProfile = {
            ...(this.profile || {}),
            id: this.profile?.id || formValue.id || undefined,
            tenantId: this.profile?.tenantId || this.tenant.id,
            tenantCode: this.profile?.tenantCode || this.tenant.tenantCode,
            airlineCode: this.profile?.airlineCode || formValue.airlineCode || this.tenant.tenantCode,
            sourceSystem: this.profile?.sourceSystem || formValue.sourceSystem || 'DEFAULT',
            enabled: this.profile?.enabled ?? true,
            pollIntervalMs: this.profile?.pollIntervalMs ?? 300000,
            channels
        };

        const request = payload.id ? this.ingestionService.update(payload.id, payload) : this.ingestionService.create(payload);
        request.subscribe((saved) => {
            this.profile = saved;
            callback?.();
        });
    }

    get channels() {
        return this.profile?.channels || [];
    }

    get selectedSourceType() {
        return this.channelForm.get('sourceType')?.value;
    }

    showHostFields() {
        const sourceType = this.selectedSourceType ?? '';
        return ['FTP', 'SFTP'].includes(sourceType);
    }

    showEmailFields() {
        return this.selectedSourceType === 'EMAIL';
    }

    showBrokerFields() {
        const sourceType = this.selectedSourceType ?? '';
        return ['JMS', 'KAFKA'].includes(sourceType);
    }

    showLocalFileFields() {
        return this.selectedSourceType === 'LOCAL_FILE';
    }
}
