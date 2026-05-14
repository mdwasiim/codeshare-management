import { Component, inject, effect, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LayoutService } from '@layout/services/layout.service';
import { TopbarComponent } from '@layout/components/topbar/topbar.component';
import { SidebarComponent } from '@layout/components/sidebar/sidebar.component';
import { FooterComponent } from '@layout/components/footer/footer.component';

@Component({
    selector: 'csm-layout',
    standalone: true,
    imports: [CommonModule, TopbarComponent, SidebarComponent, RouterModule, FooterComponent],
    templateUrl: './layout.component.html',
    styleUrls: ['./layout.component.scss']
})
export class LayoutComponent {
    layoutService = inject(LayoutService);

    constructor() {
        effect(() => {
            const state = this.layoutService.layoutState();
            if (state.mobileMenuActive) {
                document.body.classList.add('blocked-scroll');
            } else {
                document.body.classList.remove('blocked-scroll');
            }
        });
    }

    containerClass = computed(() => {
        const config = this.layoutService.layoutConfig();
        const state = this.layoutService.layoutState();
        return {
            'layout-overlay': config.menuMode === 'overlay',
            'layout-static': config.menuMode === 'static',
            'layout-static-inactive': state.staticMenuDesktopInactive && config.menuMode === 'static',
            'layout-overlay-active': state.overlayMenuActive,
            'layout-mobile-active': state.mobileMenuActive
        };
    });
}
