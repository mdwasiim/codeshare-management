import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';
import { TenantService } from '@features/access-management/identity/tenants/services/tenant.service';
import { Tenant } from '@features/access-management/models/tenant.model';
import { AuthTenantService } from '@services/auth/auth-tenant.service';

type TargetStep =
    | 'identity-providers'
    | 'oidc-config'
    | 'ingestion-profiles'
    | 'ingestion-channels'
    | 'partner-profiles'
    | 'communication-profiles'
    | 'distribution-profiles';

type LauncherConfig = {
    title: string;
    eyebrow: string;
    description: string;
    actionLabel: string;
    targetStep: TargetStep;
};

@Component({
    selector: 'tenant-onboarding-launcher-page',
    standalone: true,
    imports: [CommonModule, RouterModule, TableModule, ButtonModule, TagModule, InputTextModule],
    templateUrl: './tenant-onboarding-launcher.page.html'
})
export class TenantOnboardingLauncherPage implements OnInit {
    private readonly route = inject(ActivatedRoute);
    private readonly router = inject(Router);
    private readonly tenantService = inject(TenantService);
    private readonly authTenantService = inject(AuthTenantService);

    tenants: Tenant[] = [];
    filteredTenants: Tenant[] = [];
    loading = true;
    searchTerm = '';

    config: LauncherConfig = {
        title: 'Tenant Onboarding',
        eyebrow: 'Onboarding',
        description: 'Select a tenant to continue onboarding.',
        actionLabel: 'Open Workspace',
        targetStep: 'identity-providers'
    };

    ngOnInit(): void {
        this.config = {
            ...this.config,
            ...(this.route.snapshot.data as Partial<LauncherConfig>)
        };

        this.tenantService.getAll().subscribe((tenants) => {
            this.tenants = tenants;
            this.filteredTenants = tenants;
            this.loading = false;

            const activeTenantCode = this.authTenantService.getTenantCode();
            if (!activeTenantCode) {
                return;
            }

            const activeTenant = tenants.find((tenant) => tenant.tenantCode === activeTenantCode);
            if (activeTenant?.id) {
                this.openTarget(activeTenant);
            }
        });
    }

    onSearch(term: string) {
        this.searchTerm = term;
        const normalized = term.trim().toLowerCase();

        if (!normalized) {
            this.filteredTenants = this.tenants;
            return;
        }

        this.filteredTenants = this.tenants.filter((tenant) =>
            [tenant.name, tenant.tenantCode, tenant.region, tenant.contactEmail, tenant.status]
                .filter((value): value is string => !!value)
                .some((value) => value.toLowerCase().includes(normalized))
        );
    }

    openTarget(tenant: Tenant) {
        if (!tenant.id) return;
        void this.router.navigate(['/tenant-onboarding', tenant.id, this.config.targetStep]);
    }

    getStatusSeverity(status?: string | null) {
        switch (status) {
            case 'ACTIVE':
                return 'success';
            case 'SUSPENDED':
                return 'warn';
            case 'EXPIRED':
                return 'danger';
            default:
                return 'secondary';
        }
    }

    displayValue(value?: string | null) {
        return value?.trim() || '-';
    }
}
