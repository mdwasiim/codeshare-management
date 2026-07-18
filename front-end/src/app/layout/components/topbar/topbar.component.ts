import { Component, inject, OnInit, computed } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { StyleClassModule } from 'primeng/styleclass';
import { MenuModule } from 'primeng/menu';
import { AvatarModule } from 'primeng/avatar';

import { LayoutService } from '@layout/services/layout.service';
import { AppMenuModel } from '@features/administration/access-management/models/app-menu.model';
import { LayoutMenuService } from '@layout/services/layout-menu.service';
import { AuthService } from '@services/auth/auth.service';
import { AuthTokenService } from '@services/auth/auth-token.service';

@Component({
    selector: 'app-topbar',
    standalone: true,
    imports: [CommonModule, StyleClassModule, MenuModule, AvatarModule],
    templateUrl: './topbar.component.html',
    styleUrls: ['./topbar.component.scss']
})
export class TopbarComponent implements OnInit {
    private router = inject(Router);
    private authService = inject(AuthService);
    private menuService = inject(LayoutMenuService);
    private tokenService = inject(AuthTokenService);

    constructor(public layout: LayoutService) {}

    // =========================
    // STATE
    // =========================

    username = '';
    loggingOut = false;

    rootMenus$ = this.menuService.getRootMenus();
    selectedRoot$ = this.menuService.selectedRootMenu$;

    initials = computed(() =>
        this.username
            ? this.username
                  .split(' ')
                  .map((x) => x[0])
                  .join('')
                  .toUpperCase()
            : 'U'
    );

    userMenuItems: MenuItem[] = [];

    userInitialsCss(): string {
        return `"${this.initials()}"`;
    }

    ngOnInit() {
        this.username = this.tokenService.username || 'User';
        this.buildUserMenu();
    }

    // =========================
    // MENU
    // =========================

    selectMenu(menu: AppMenuModel) {
        this.menuService.setSelectedRoot(menu);

        const route = this.getFirstNavigableRoute(menu);

        if (route && this.router.url !== route) {
            this.router.navigate([route]).catch(() => {
                console.warn('Navigation failed:', route);
            });
        }
    }

    isActive(menu: AppMenuModel, selected: AppMenuModel | null): boolean {
        if (!selected) return false;

        if (selected.id && menu.id) {
            return selected.id === menu.id;
        }

        return selected.code === menu.code;
    }

    // =========================
    // USER MENU
    // =========================

    private buildUserMenu() {
        const fullName = this.tokenService.username ?? this.username ?? 'User';

        const role = this.tokenService.roles?.length
            ? this.tokenService.roles
                  .map((r) => r.replace('_', ' '))
                  .map((r) => r.toLowerCase())
                  .map((r) => r.charAt(0).toUpperCase() + r.slice(1))
                  .join(', ')
            : 'User';

        this.userMenuItems = [
            {
                label: fullName,
                styleClass: 'user-menu-header',
                disabled: true
            },
            {
                label: role,
                styleClass: 'user-menu-subheader',
                disabled: true
            },
            { separator: true },
            {
                label: 'Profile',
                icon: 'pi pi-user',
                command: () => this.router.navigate(['/profile'])
            },
            {
                label: 'Settings',
                icon: 'pi pi-cog',
                command: () => this.router.navigate(['/settings'])
            },
            { separator: true },
            {
                label: 'Logout',
                icon: 'pi pi-sign-out',
                styleClass: 'logout-item',
                command: () => this.logout()
            }
        ];
    }

    logout() {
        if (this.loggingOut) return;

        this.loggingOut = true;

        this.authService.logout().subscribe({
            next: () => this.safeRedirectToLogin(),
            error: () => this.safeRedirectToLogin(),
            complete: () => (this.loggingOut = false)
        });
    }

    private safeRedirectToLogin() {
        this.router.navigate(['/login']);
    }

    // =========================
    // THEME
    // =========================

    toggleDarkMode() {
        this.layout.toggleTheme();
    }

    // =========================
    // NAVIGATION HELPER
    // =========================

    private getFirstNavigableRoute(menu: AppMenuModel): string | null {
        if (menu.items?.length) {
            for (const child of menu.items) {
                const childRoute = this.getFirstNavigableRoute(child);
                if (childRoute) {
                    return childRoute;
                }
            }
        }

        return menu.navigationType === 'INTERNAL_LINK' ? menu.frontendPath ?? null : null;
    }
}

