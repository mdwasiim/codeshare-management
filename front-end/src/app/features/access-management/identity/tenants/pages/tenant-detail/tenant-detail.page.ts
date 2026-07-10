import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, NavigationEnd, Router, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { filter, switchMap } from 'rxjs/operators';
import { forkJoin, of } from 'rxjs';
import { TenantService } from '../../services/tenant.service';
import { Tenant, TenantAuthContext } from '@features/access-management/models/tenant.model';
import { TenantPartner } from '@features/partner-management/tenant-partners/models/tenant-partner.model';
import { TenantPartnerService } from '@features/partner-management/tenant-partners/services/tenant-partners.service';

type TenantWorkspaceTab = {
    label: string;
    route: string;
    description: string;
};

@Component({
    selector: 'tenant-detail-page',
    standalone: true,
    imports: [CommonModule, RouterModule, ButtonModule, TagModule],
    templateUrl: './tenant-detail.page.html',
    styleUrl: './tenant-detail.page.scss'
})
export class TenantDetailPage {
    private readonly route = inject(ActivatedRoute);
    private readonly router = inject(Router);
    private readonly tenantService = inject(TenantService);
    private readonly tenantPartnerService = inject(TenantPartnerService);

    readonly tabs: TenantWorkspaceTab[] = [
        { label: 'Overview', route: 'overview', description: 'Tenant profile, subscription, and operating region' },
        { label: 'Identity', route: 'identity', description: 'Authentication sources and OIDC configuration' },
        { label: 'Ingestion', route: 'ingestion', description: 'Schedule ingestion profiles, channels, and run operations' },
        { label: 'Partners', route: 'partners', description: 'Codeshare agreement and partner configuration' }
    ];

    tenant: Tenant | null = null;
    authContext: TenantAuthContext | null = null;
    partners: TenantPartner[] = [];
    loading = true;
    activeRoute = 'overview';

    constructor() {
        this.route.paramMap
            .pipe(
                switchMap((params) => {
                    const id = params.get('id');
                    if (!id) {
                        return of(null);
                    }
                    this.loading = true;
                    return this.tenantService.getById(id);
                }),
                switchMap((tenant) => {
                    if (!tenant) {
                        return of({ tenant: null, authContext: null, partners: [] as TenantPartner[] });
                    }

                    const authContext$ = tenant.tenantCode ? this.tenantService.getAuthContextByCode(tenant.tenantCode) : of(null);
                    const partners$ = this.tenantPartnerService.getAll();

                    return forkJoin({
                        tenant: of(tenant),
                        authContext: authContext$,
                        partners: partners$
                    });
                })
            )
            .subscribe((result) => {
                this.tenant = result?.tenant ?? null;
                this.authContext = result?.authContext ?? null;
                this.partners = (result?.partners ?? []).filter((partner) => partner.homeAirlineCode === this.tenant?.tenantCode);
                this.loading = false;
            });

        this.router.events.pipe(filter((event) => event instanceof NavigationEnd)).subscribe(() => {
            const childPath = this.route.firstChild?.snapshot.routeConfig?.path;
            this.activeRoute = childPath || 'overview';
        });
    }

    openEdit() {
        if (!this.tenant?.id) return;
        void this.router.navigate(['/tenants', this.tenant.id, 'edit']);
    }

    backToList() {
        void this.router.navigate(['/tenants']);
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

    get identityProviderCount() {
        return this.authContext?.identityProviders?.length ?? (this.tenant?.authSource ? 1 : 0);
    }
}
