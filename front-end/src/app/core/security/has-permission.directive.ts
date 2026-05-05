import {Directive, Input, TemplateRef, ViewContainerRef} from "@angular/core";
import {PermissionService} from "@core/security/permission.service";

@Directive({
    selector: '[appHasPermission]'
})
export class HasPermissionDirective {

    @Input() set appHasPermission(value: string) {
        if (this.permissionService.hasRaw(value)) {
            this.viewContainer.createEmbeddedView(this.templateRef);
        } else {
            this.viewContainer.clear();
        }
    }

    constructor(
        private templateRef: TemplateRef<any>,
        private viewContainer: ViewContainerRef,
        private permissionService: PermissionService
    ) {}
}
