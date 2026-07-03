import { Component, inject, input, computed } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RippleModule } from 'primeng/ripple';
import { LayoutService } from '@layout/services/layout.service';
import { AppMenuModel } from '@features/access-management/models/app-menu.model';

@Component({
    selector: '[app-menuitem]',
    standalone: true,
    imports: [CommonModule, RouterModule, RippleModule],
    host: {
        '[class.active-menuitem]': 'isActive()',
        '[class.layout-root-menuitem]': 'root()'
    },
    templateUrl: './app-menu-item.component.html',
    styleUrls: ['./app-menu-item.component.scss']
})
export class AppMenuItemComponent {
    private router = inject(Router);
    layoutService = inject(LayoutService);

    item = input<AppMenuModel>({} as AppMenuModel);
    root = input<boolean>(false);

    // =========================
    // VISIBILITY
    // =========================
    isVisible = computed(() => this.item()?.visible !== false);

    hasChildren = computed(() => {
        return (this.item()?.items ?? []).length > 0;
    });

    isExpanded = computed(() => {
        return this.hasChildren() && this.isActive();
    });

    // =========================
    // ACTIVE STATE (Router Driven)
    // =========================
    isActive = computed(() => {
        return this.isRouteMatch(this.item());
    });

    // =========================
    // CLICK HANDLER
    // =========================
    itemClick() {
        const item = this.item();

        if (!item) return;

        if (item.routerLink) {
            const link = Array.isArray(item.routerLink) ? item.routerLink : [item.routerLink];

            this.router.navigate(link);
        }

        // Close sidebar (mobile / overlay)
        this.layoutService.closeSidebar();
    }

    onBranchClick(event: Event) {
        event.preventDefault();

        const item = this.item();
        if (!item || !this.hasChildren()) return;

        item.expanded = true;

        const link = Array.isArray(item.routerLink) ? item.routerLink : [item.routerLink ?? item.route];
        const target = link[0];

        if (target) {
            this.router.navigate(link.filter(Boolean) as string[]);
        }
    }

    shouldRenderChildren(): boolean {
        const item = this.item();

        if (!item || !this.hasChildren() || !this.isVisible()) {
            return false;
        }

        if (this.root()) {
            return !!item.expanded;
        }

        return !!item.expanded || this.hasActiveDescendant(item);
    }

    childTrackKey(child: AppMenuModel, index: number): string {
        return child.id ?? child.code ?? child.route ?? `${child.label}-${index}`;
    }

    private hasActiveDescendant(item: AppMenuModel): boolean {
        const children = item.items ?? [];

        return children.some((child) => this.isRouteMatch(child) || this.hasActiveDescendant(child));
    }

    private isRouteMatch(item: AppMenuModel | undefined): boolean {
        if (!item) return false;

        const link = Array.isArray(item.routerLink) ? item.routerLink[0] : (item.routerLink ?? item.route);

        if (!link) return false;

        return this.matchesPath(this.router.url, link);
    }

    private matchesPath(currentUrl: string, link: string): boolean {
        if (!currentUrl || !link) return false;
        if (currentUrl === link) return true;

        return currentUrl.startsWith(`${link}/`) || currentUrl.startsWith(`${link}?`);
    }
}
