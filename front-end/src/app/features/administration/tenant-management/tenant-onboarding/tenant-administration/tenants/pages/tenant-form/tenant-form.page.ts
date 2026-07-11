import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { SelectModule } from 'primeng/select';
import { CheckboxModule } from 'primeng/checkbox';

import { BaseCrudForm } from '@shared/components/base/base-form.component';
import { AppFormSectionComponent } from '@shared/components/form-section/app-form-section.component';

import { Tenant, TenantPlan, TenantStatus, AuthSource } from '@features/administration/tenant-management/models/tenant.model';
import { TenantService } from '../../services/tenant.service';

@Component({
    selector: 'tenant-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputTextModule,
        TextareaModule,
        SelectModule,
        CheckboxModule,
        AppFormSectionComponent
    ],
    templateUrl: './tenant-form.page.html'
})
export class TenantFormPage extends BaseCrudForm<Tenant> {
    private readonly fb = inject(FormBuilder);
    private readonly service = inject(TenantService);

    statusOptions: { label: string; value: TenantStatus }[] = [
        { label: 'Active', value: 'ACTIVE' },
        { label: 'Suspended', value: 'SUSPENDED' },
        { label: 'Expired', value: 'EXPIRED' },
        { label: 'Deleted', value: 'DELETED' }
    ];

    planOptions: { label: string; value: TenantPlan }[] = [
        { label: 'Free', value: 'FREE' },
        { label: 'Pro', value: 'PRO' },
        { label: 'Enterprise', value: 'ENTERPRISE' }
    ];

    authSourceOptions: { label: string; value: AuthSource }[] = [
        { label: 'Internal', value: AuthSource.INTERNAL },
        { label: 'LDAP', value: AuthSource.LDAP },
        { label: 'Azure AD', value: AuthSource.AZURE },
        { label: 'Keycloak', value: AuthSource.KEYCLOAK },
        { label: 'Okta', value: AuthSource.OKTA },
        { label: 'OIDC Generic', value: AuthSource.OIDC_GENERIC }
    ];

    get oidcRequired(): boolean {
        const authSource = this.form?.get('authSource')?.value as AuthSource;
        return authSource === AuthSource.AZURE || authSource === AuthSource.KEYCLOAK || authSource === AuthSource.OKTA || authSource === AuthSource.OIDC_GENERIC;
    }

    override buildForm(): void {
        this.form = this.fb.group({
            id: [null as string | null],
            name: ['', [Validators.required, Validators.maxLength(200)]],
            tenantCode: ['', [Validators.required, Validators.maxLength(100)]],
            description: ['', Validators.maxLength(500)],
            plan: [null as TenantPlan | null],
            status: ['ACTIVE' as TenantStatus, Validators.required],
            subscriptionStart: [''],
            subscriptionEnd: [''],
            trial: [false],
            contactEmail: ['', [Validators.email, Validators.maxLength(200)]],
            contactPhone: ['', Validators.maxLength(50)],
            logoUrl: ['', Validators.maxLength(500)],
            region: ['', Validators.maxLength(100)],
            authSource: [AuthSource.INTERNAL, Validators.required],
            oidcConfig: this.fb.group({
                issuerUri: ['', Validators.maxLength(500)],
                authorizationUri: ['', Validators.maxLength(500)],
                tokenUri: ['', Validators.maxLength(500)],
                jwkSetUri: ['', Validators.maxLength(500)],
                clientId: ['', Validators.maxLength(200)],
                clientSecretRef: ['', Validators.maxLength(200)],
                redirectUri: ['', Validators.maxLength(500)],
                scopes: ['', Validators.maxLength(200)],
                enforceRedirectUri: [false]
            })
        });
    }

    override patchForm(data: Tenant): void {
        this.form.patchValue({
            ...data,
            subscriptionStart: this.toDateTimeLocal(data.subscriptionStart),
            subscriptionEnd: this.toDateTimeLocal(data.subscriptionEnd),
            oidcConfig: {
                issuerUri: '',
                authorizationUri: '',
                tokenUri: '',
                jwkSetUri: '',
                clientId: '',
                clientSecretRef: '',
                redirectUri: '',
                scopes: '',
                enforceRedirectUri: false,
                ...data.oidcConfig
            }
        });

        if (data.id) {
            this.form.get('tenantCode')?.disable();
        }
    }

    private toDateTimeLocal(value?: string | null): string {
        if (!value) {
            return '';
        }
        const date = new Date(value);
        if (isNaN(date.getTime())) {
            return '';
        }
        const pad = (n: number) => String(n).padStart(2, '0');
        return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
    }

    private parseDateTimeLocal(value?: string | null): string | undefined {
        if (!value) {
            return undefined;
        }
        const date = new Date(value);
        return isNaN(date.getTime()) ? undefined : date.toISOString();
    }

    override fetchById(id: string) {
        return this.service.getById(id);
    }

    override create(payload: Tenant) {
        return this.service.create(this.mapToModel(payload));
    }

    override update(id: string, payload: Tenant) {
        return this.service.update(id, this.mapToModel(payload));
    }

    private mapToModel(formValue: Tenant): Tenant {
        const value = { ...formValue };

        if (value.tenantCode) {
            value.tenantCode = value.tenantCode.trim().toUpperCase();
        }

        value.subscriptionStart = this.parseDateTimeLocal(value.subscriptionStart);
        value.subscriptionEnd = this.parseDateTimeLocal(value.subscriptionEnd);

        if (value.authSource === AuthSource.INTERNAL) {
            value.oidcConfig = undefined;
        } else {
            const oidc = value.oidcConfig;
            if (oidc) {
                value.oidcConfig = {
                    issuerUri: oidc.issuerUri?.trim() || undefined,
                    authorizationUri: oidc.authorizationUri?.trim() || undefined,
                    tokenUri: oidc.tokenUri?.trim() || undefined,
                    jwkSetUri: oidc.jwkSetUri?.trim() || undefined,
                    clientId: oidc.clientId?.trim() || undefined,
                    clientSecretRef: oidc.clientSecretRef?.trim() || undefined,
                    redirectUri: oidc.redirectUri?.trim() || undefined,
                    scopes: oidc.scopes?.trim() || undefined,
                    enforceRedirectUri: !!oidc.enforceRedirectUri
                };
            }
        }

        return value;
    }
}
