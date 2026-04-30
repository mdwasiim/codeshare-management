import {Component, inject, input, computed, signal} from '@angular/core';
import {NavigationEnd, Router, RouterModule} from '@angular/router';
import { filter } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { RippleModule } from 'primeng/ripple';
import { LayoutService } from '@layout/services/layout.service';
import {AppMenuModel} from "@shared/models/app-menu.model";

@Component({
    selector: '[app-menuitem]',
    imports: [CommonModule, RouterModule, RippleModule],
    host: {
        '[class.active-menuitem]': 'isActive()',
        '[class.layout-root-menuitem]': 'root()'
    },
    standalone: true,
    templateUrl: './app-menu-item.component.html',
    styleUrls: ['./app-menu-item.component.scss'],
   /* animations: [
        trigger('children', [
            state('collapsed', style({ height: '0' })),
            state('expanded', style({ height: '*' })),
            transition('collapsed <=> expanded', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)'))
        ])
    ]*/
})
export class  AppMenuItemComponent  {
    layoutService = inject(LayoutService);

    router = inject(Router);

    item = input<AppMenuModel>({} as AppMenuModel);

    root = input<boolean>(false);

    parentPath = input<string>('');

    isVisible = computed(() => this.item()?.visible !== false);

    hasChildren = computed(() => {
        const items = this.item()?.items ?? [];
        return items.length > 0;
    });

    hasRouterLink = computed(() => {
        const link = this.item()?.routerLink;
        return Array.isArray(link) && !!link[0];
    });

    fullPath = computed(() => {
        const itemPath = this.item()?.path;
        if (!itemPath) return this.parentPath();
        const parent = this.parentPath();
        if (parent && !itemPath.startsWith(parent)) {
            return parent + itemPath;
        }
        return itemPath;
    });

    isActive = computed(() => {
        const activePath = this.layoutService.layoutState().activePath;
        if (this.item()?.path) {
            return activePath?.startsWith(this.fullPath() ?? '') ?? false;
        }
        return false;
    });

    initialized = signal<boolean>(false);

    constructor() {
        this.router.events
            .pipe(filter((event) => event instanceof NavigationEnd))
            .subscribe(() => {
                if (this.hasRouterLink()) {   // ✅ safe check
                    this.updateActiveStateFromRoute();
                }
            });
    }

    ngOnInit() {
        if (this.hasRouterLink()) {
            this.updateActiveStateFromRoute();
        }
    }

    ngAfterViewInit() {
        setTimeout(() => {
            this.initialized.set(true);
        });
    }

    updateActiveStateFromRoute() {
        const item = this.item();

        const link = item?.routerLink?.[0];

        // 🔥 FULL SAFETY CHECK
        if (!link || typeof link !== 'string') return;

        const isRouteActive = this.router.isActive(link, {
            paths: 'exact',
            queryParams: 'ignored',
            matrixParams: 'ignored',
            fragment: 'ignored'
        });

        if (isRouteActive) {
            const parentPath = this.parentPath();
            if (parentPath) {
                this.layoutService.layoutState.update((val) => ({
                    ...val,
                    activePath: parentPath
                }));
            }
        }
    }

    itemClick(event: Event) {
        const item = this.item();

        if (item?.active) {
            event.preventDefault();
            return;
        }

        if (item?.command) {
            item.command({ originalEvent: event, item: item });
        }

        if (this.hasChildren()) {
            if (this.isActive()) {
                this.layoutService.layoutState.update((val) => ({
                    ...val,
                    activePath: this.parentPath()
                }));
            } else {
                this.layoutService.layoutState.update((val) => ({
                    ...val,
                    activePath: this.fullPath(),
                    menuHoverActive: true
                }));
            }
        } else {
            this.layoutService.layoutState.update((val) => ({
                ...val,
                overlayMenuActive: false,
                staticMenuMobileActive: false,
                mobileMenuActive: false,
                menuHoverActive: false
            }));
        }
    }
}
