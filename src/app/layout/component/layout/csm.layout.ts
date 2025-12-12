import { Component, Renderer2, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { filter, Subscription } from 'rxjs';
import { CsmFooter } from '../footer/csm.footer';
import { CsmTopbar } from '../topbar/csm.topbar';
import { CsmSidebar } from '../sidebar/csm.sidebar';
import { CsmLayoutService } from '@/layout/service/csm.layout.service';

@Component({
    selector: 'csm-layout',
    standalone: true,
    imports: [CommonModule, CsmTopbar, CsmSidebar, RouterModule, CsmFooter],
    templateUrl: './csm.layout.html',
    styleUrls: ['./csm.layout.scss']
})
export class CsmLayout {
    overlayMenuOpenSubscription: Subscription;
    menuOutsideClickListener: any;

    @ViewChild(CsmSidebar) csmSidebar!: CsmSidebar;
    @ViewChild(CsmTopbar) csmTopBar!: CsmTopbar;

    constructor(
        public csmLayoutService: CsmLayoutService,
        public renderer: Renderer2,
        public router: Router
    ) {
        this.overlayMenuOpenSubscription = this.csmLayoutService.overlayOpen$.subscribe(() => {
            if (!this.menuOutsideClickListener) {
                this.menuOutsideClickListener = this.renderer.listen('document', 'click', (event) => {
                    if (this.isOutsideClicked(event)) {
                        this.hideMenu();
                    }
                });
            }

            if (this.csmLayoutService.layoutState().staticMenuMobileActive) {
                this.blockBodyScroll();
            }
        });

        this.router.events
            .pipe(filter((event) => event instanceof NavigationEnd))
            .subscribe(() => {
                this.hideMenu();
            });
    }

    isOutsideClicked(event: MouseEvent) {
        const sidebarEl = document.querySelector('.layout-sidebar');
        const topbarEl = document.querySelector('.layout-menu-button');
        const eventTarget = event.target as Node;

        return !(
            sidebarEl?.isSameNode(eventTarget) ||
            sidebarEl?.contains(eventTarget) ||
            topbarEl?.isSameNode(eventTarget) ||
            topbarEl?.contains(eventTarget)
        );
    }

    hideMenu() {
        this.csmLayoutService.layoutState.update((prev) => ({
            ...prev,
            overlayMenuActive: false,
            staticMenuMobileActive: false,
            menuHoverActive: false
        }));

        if (this.menuOutsideClickListener) {
            this.menuOutsideClickListener();
            this.menuOutsideClickListener = null;
        }

        this.unblockBodyScroll();
    }

    blockBodyScroll(): void {
        if (document.body.classList) {
            document.body.classList.add('blocked-scroll');
        } else {
            document.body.className += ' blocked-scroll';
        }
    }

    unblockBodyScroll(): void {
        if (document.body.classList) {
            document.body.classList.remove('blocked-scroll');
        } else {
            document.body.className = document.body.className.replace(
                new RegExp(
                    '(^|\\b)' + 'blocked-scroll'.split(' ').join('|') + '(\\b|$)',
                    'gi'
                ),
                ' '
            );
        }
    }

    get containerClass() {
        return {
            'layout-overlay': this.csmLayoutService.layoutConfig().menuMode === 'overlay',
            'layout-static': this.csmLayoutService.layoutConfig().menuMode === 'static',
            'layout-static-inactive':
                this.csmLayoutService.layoutState().staticMenuDesktopInactive &&
                this.csmLayoutService.layoutConfig().menuMode === 'static',
            'layout-overlay-active': this.csmLayoutService.layoutState().overlayMenuActive,
            'layout-mobile-active': this.csmLayoutService.layoutState().staticMenuMobileActive
        };
    }

    ngOnDestroy() {
        if (this.overlayMenuOpenSubscription) {
            this.overlayMenuOpenSubscription.unsubscribe();
        }

        if (this.menuOutsideClickListener) {
            this.menuOutsideClickListener();
        }
    }
}
