import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';

import { PermissionService } from '@core/security/permission.service';

@Directive({
    selector: '[csmHasPermission]',
    standalone: true
})
export class HasPermissionDirective {
    private rendered = false;

    constructor(
        private templateRef: TemplateRef<any>,
        private viewContainer: ViewContainerRef,
        private permissionService: PermissionService
    ) {}

    // =========================
    // SINGLE PERMISSION
    // =========================
    @Input()
    set csmHasPermission(permission: string | null) {
        if (!permission) {
            this.hide();
            return;
        }

        const hasAccess = this.permissionService.hasRawPermission(permission);

        this.updateView(hasAccess);
    }

    // =========================
    // ANY PERMISSION
    // =========================
    @Input()
    set csmHasAnyPermission(permissions: string[] | null) {
        if (!permissions?.length) {
            this.hide();
            return;
        }

        const hasAccess = this.permissionService.hasAnyPermission(permissions);

        this.updateView(hasAccess);
    }

    // =========================
    // ALL PERMISSIONS
    // =========================
    @Input()
    set csmHasAllPermissions(permissions: string[] | null) {
        if (!permissions?.length) {
            this.hide();
            return;
        }

        const hasAccess = this.permissionService.hasAllPermissions(permissions);

        this.updateView(hasAccess);
    }

    // =========================
    // INTERNAL
    // =========================
    private updateView(hasAccess: boolean): void {
        if (hasAccess && !this.rendered) {
            this.viewContainer.createEmbeddedView(this.templateRef);

            this.rendered = true;
        } else if (!hasAccess && this.rendered) {
            this.hide();
        }
    }

    private hide(): void {
        this.viewContainer.clear();

        this.rendered = false;
    }
}
