import { Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { BaseCrudForm } from '@shared/components/base/base-form.component';
import { TenantService } from '../../services/tenant.service';
import { AuthSource, OidcConfig, Tenant, TenantPlan, TenantStatus } from '@features/access-management/models/tenant.model';
import { SelectModule } from 'primeng/select';
import { AppFormSectionComponent } from '@shared/components/form-section/app-form-section.component';

@Component({
    selector: 'tenant-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, InputTextModule, ButtonModule, DialogModule, SelectModule, AppFormSectionComponent],
    templateUrl: './tenant-form.page.html'
})
export class TenantFormPage extends BaseCrudForm<Tenant> {
    private fb = inject(FormBuilder);
    private service = inject(TenantService);

    statusOptions = [
        { label: 'Active', value: TenantStatus.ACTIVE },
        { label: 'Suspended', value: TenantStatus.SUSPENDED },
        { label: 'Expired', value: TenantStatus.EXPIRED },
        { label: 'Deleted', value: TenantStatus.DELETED }
    ];

    planOptions = [
        { label: 'Free', value: TenantPlan.FREE },
        { label: 'Pro', value: TenantPlan.PRO },
        { label: 'Enterprise', value: TenantPlan.ENTERPRISE }
    ];

    authSourceOptions = [
        { label: 'Internal', value: AuthSource.INTERNAL },
        { label: 'LDAP', value: AuthSource.LDAP },
        { label: 'Azure AD', value: AuthSource.AZURE },
        { label: 'Keycloak', value: AuthSource.KEYCLOAK },
        { label: 'Okta', value: AuthSource.OKTA },
        { label: 'OpenID Connect', value: AuthSource.OIDC_GENERIC }
    ];

    override ngOnInit() {
        this.buildForm();
    }

    buildForm(): void {
        this.form = this.fb.group({
            id: [null],
            name: ['', Validators.required],
            code: ['', Validators.required],
            description: [''],
            contactEmail: [''],
            contactPhone: [''],
            region: [''],
            status: [TenantStatus.ACTIVE],
            plan: [TenantPlan.PRO],
            authSource: [AuthSource.INTERNAL],
            oidcIssuerUri: [''],
            oidcAuthorizationUri: [''],
            oidcTokenUri: [''],
            oidcJwkSetUri: [''],
            oidcClientId: [''],
            oidcClientSecretRef: [''],
            oidcRedirectUri: [''],
            oidcScopes: ['openid profile email']
        });
    }

    patchForm(data: Tenant): void {
        this.form.patchValue({
            ...data,
            oidcIssuerUri: data.oidcConfig?.issuerUri || '',
            oidcAuthorizationUri: data.oidcConfig?.authorizationUri || '',
            oidcTokenUri: data.oidcConfig?.tokenUri || '',
            oidcJwkSetUri: data.oidcConfig?.jwkSetUri || '',
            oidcClientId: data.oidcConfig?.clientId || '',
            oidcClientSecretRef: data.oidcConfig?.clientSecretRef || '',
            oidcRedirectUri: data.oidcConfig?.redirectUri || '',
            oidcScopes: data.oidcConfig?.scopes || 'openid profile email'
        });
    }

    fetchById(id: string) {
        return this.service.getById(id);
    }

    create(payload: Tenant) {
        return this.service.create(this.toPayload(payload));
    }

    update(id: string, payload: Tenant) {
        return this.service.update(id, this.toPayload(payload));
    }

    isOidcProvider(): boolean {
        return [AuthSource.AZURE, AuthSource.KEYCLOAK, AuthSource.OKTA, AuthSource.OIDC_GENERIC].includes(this.form.get('authSource')?.value);
    }

    isLdapProvider(): boolean {
        return this.form.get('authSource')?.value === AuthSource.LDAP;
    }

    private toPayload(payload: Tenant): Tenant {
        const authSource = this.form.get('authSource')?.value as AuthSource;
        const normalizedPayload: Tenant = {
            ...payload,
            code: payload.code?.trim().toUpperCase(),
            authSource
        };

        if (this.isOidcProvider()) {
            normalizedPayload.oidcConfig = this.buildOidcConfig();
        } else if (this.isLdapProvider()) {
            normalizedPayload.oidcConfig = {
                issuerUri: this.form.get('oidcIssuerUri')?.value || '',
                scopes: this.form.get('oidcScopes')?.value || ''
            } as OidcConfig;
        } else {
            delete normalizedPayload.oidcConfig;
        }

        return normalizedPayload;
    }

    private buildOidcConfig(): OidcConfig {
        return {
            issuerUri: this.form.get('oidcIssuerUri')?.value || '',
            authorizationUri: this.form.get('oidcAuthorizationUri')?.value || '',
            tokenUri: this.form.get('oidcTokenUri')?.value || '',
            jwkSetUri: this.form.get('oidcJwkSetUri')?.value || '',
            clientId: this.form.get('oidcClientId')?.value || '',
            clientSecretRef: this.form.get('oidcClientSecretRef')?.value || '',
            redirectUri: this.form.get('oidcRedirectUri')?.value || '',
            scopes: this.form.get('oidcScopes')?.value || 'openid profile email',
            enforceRedirectUri: true
        };
    }
}
