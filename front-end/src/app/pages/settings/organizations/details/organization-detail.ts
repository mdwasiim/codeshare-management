import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'organization-detail',
    standalone: true,
    imports: [CommonModule, CardModule, ButtonModule],
    template: `
        <p-card header="Organization Details">
            <div class="field"><b>ID:</b> {{ organizationId }}</div>
            <div class="field"><b>Name:</b> {{ organization?.name }}</div>
            <div class="field"><b>Code:</b> {{ organization?.code }}</div>
            <div class="field"><b>Status:</b> {{ organization?.status }}</div>

            <button
                pButton
                label="Back"
                icon="pi pi-arrow-left"
                class="mt-3"
                routerLink="/organizations">
            </button>
        </p-card>
    `
})
export class OrganizationDetail implements OnInit {

    organizationId!: string;
    organization: any;

    constructor(private route: ActivatedRoute) {}

    ngOnInit(): void {
        this.organizationId = this.route.snapshot.paramMap.get('id')!;

        // Mock data (replace with API call)
        this.organization = {
            id: this.organizationId,
            name: 'Qatar Airways',
            code: 'QR',
            status: 'ACTIVE'
        };
    }
}
