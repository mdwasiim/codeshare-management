import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { AppTokenService } from '@services/auth/app-token.service';

@Directive({
    selector: '[hasPermission]'
})
export class HasPermissionDirective {

    constructor(
        private templateRef: TemplateRef<any>,
        private viewContainer: ViewContainerRef,
        private tokenService: AppTokenService
    ) {}

    @Input() set hasPermission(permission: string) {
        const hasAccess = this.tokenService.permissions.includes(permission);

        this.viewContainer.clear();

        if (hasAccess) {
            this.viewContainer.createEmbeddedView(this.templateRef);
        }
    }
}
