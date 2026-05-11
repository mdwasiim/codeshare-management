import { Component, inject, OnInit, computed } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { StyleClassModule } from 'primeng/styleclass';
import { MenuModule } from 'primeng/menu';
import { AvatarModule } from 'primeng/avatar';

import { LayoutService } from '@layout/services/layout.service';
import { AuthService } from '@features/access-management/auth/services/auth.service';
import { AppMenuModel } from '@features/access-management/iam/models/app-menu.model';
import { LayoutMenuService } from '@layout/services/layout-menu.service';
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
                .map(x => x[0])
                .join('')
                .toUpperCase()
            : 'U'
    );

    userMenuItems: MenuItem[] = [];

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

        // ✅ avoid unnecessary navigation
        if (route && this.router.url !== route) {
            this.router.navigate([route]).catch(() => {
                console.warn('Navigation failed:', route);
            });
        }
    }

    isActive(menu: AppMenuModel, selected: AppMenuModel | null): boolean {
        return selected?.id === menu.id;
    }

    // =========================
    // USER MENU
    // =========================

    private buildUserMenu() {
        const fullName =
            this.tokenService.username ??
            this.username ??
            'User';

        const role =
            this.tokenService.roles?.length
                ? this.tokenService.roles
                    .map(r => r.replace('_', ' '))
                    .map(r => r.toLowerCase())
                    .map(r => r.charAt(0).toUpperCase() + r.slice(1))
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

    private navigate(path: string) {
        if (this.router.url !== path) {
            this.router.navigate([path]);
        }
    }

    logout() {
        if (this.loggingOut) return;

        this.loggingOut = true;

        this.authService.logout().subscribe({
            next: () => this.safeRedirectToLogin(),
            error: () => this.safeRedirectToLogin(),
            complete: () => (this.loggingOut = false) // ✅ reset state
        });
    }

    private safeRedirectToLogin() {
        this.router.navigate(['/auth/login']);
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
        // ✅ BFS (safer than recursion for deep trees)
        const queue: AppMenuModel[] = [menu];

        while (queue.length) {
            const current = queue.shift()!;

            if (current.route) {
                return current.route;
            }

            if (current.items?.length) {
                queue.push(...current.items);
            }
        }

        return null;
    }
}
