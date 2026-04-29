import { Component, HostBinding, Input, OnInit, OnDestroy } from '@angular/core';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { RippleModule } from 'primeng/ripple';
import { LayoutService } from '@layout/services/layout.service';
import { MenuItemModel } from '@features/iam/menu/models/menu-item.model';

@Component({
    selector: 'app-menu-item',
    standalone: true,
    imports: [CommonModule, RouterModule, RippleModule],
    templateUrl: './app-menu.component.html',
    styleUrls: ['./app-menu.component.scss'],
    animations: [
        trigger('children', [
            state('collapsed', style({ height: '0' })),
            state('expanded', style({ height: '*' })),
            transition('collapsed <=> expanded', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)'))
        ])
    ]
})
export class AppMenuComponent implements OnInit, OnDestroy {

    @Input() item!: MenuItemModel;
    @Input() index!: number;
    @Input() parentKey!: string;

    @Input()
    @HostBinding('class.layout-root-app-menu-item-list')
    root!: boolean;

    active = false;
    key: string = '';

    private subscriptions = new Subscription();

    constructor(
        public router: Router,
        private layoutService: LayoutService
    ) {
        this.subscriptions.add(
            this.layoutService.menuSource$.subscribe((value) => {
                Promise.resolve(null).then(() => {
                    if (value.routeEvent) {
                        this.active =
                            value.key === this.key ||
                            value.key.startsWith(this.key + '-');
                    } else {
                        if (value.key !== this.key && !value.key.startsWith(this.key + '-')) {
                            this.active = false;
                        }
                    }
                });
            })
        );

        this.subscriptions.add(
            this.layoutService.resetSource$.subscribe(() => {
                this.active = false;
            })
        );

        this.subscriptions.add(
            this.router.events
                .pipe(filter((event) => event instanceof NavigationEnd))
                .subscribe(() => {
                    if (this.item.routerLink) {
                        this.updateActiveStateFromRoute();
                    }
                })
        );
    }

    ngOnInit() {
        this.key = this.parentKey
            ? `${this.parentKey}-${this.index}`
            : `${this.index}`;

        if (this.item.routerLink) {
            this.updateActiveStateFromRoute();
        }
    }

    updateActiveStateFromRoute() {
        if (!this.item.routerLink) return;

        const link = Array.isArray(this.item.routerLink)
            ? this.item.routerLink[0]
            : this.item.routerLink;

        const activeRoute = this.router.isActive(link, {
            paths: 'exact',
            queryParams: 'ignored',
            matrixParams: 'ignored',
            fragment: 'ignored'
        });

        if (activeRoute) {
            this.layoutService.onMenuStateChange({
                key: this.key,
                routeEvent: true
            });
        }
    }

    itemClick(event: Event) {

        if ((this.item as any).disabled) {
            event.preventDefault();
            return;
        }

        if ((this.item as any).command) {
            (this.item as any).command({
                originalEvent: event,
                item: this.item
            });
        }

        // 🔥 IMPORTANT FIX
        if (this.item.items) {
            event.preventDefault();
            this.active = !this.active;

            this.layoutService.onMenuStateChange({ key: this.key });
            return;
        }

        this.layoutService.onMenuStateChange({ key: this.key });
    }

    get submenuAnimation() {
        return this.root ? 'expanded' : this.active ? 'expanded' : 'collapsed';
    }

    @HostBinding('class.active-app-menu-item-list')
    get activeClass() {
        return this.active && !this.root;
    }

    ngOnDestroy() {
        this.subscriptions.unsubscribe();
    }
}
