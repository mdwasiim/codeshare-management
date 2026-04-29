import { Component, inject } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { StyleClassModule } from 'primeng/styleclass';
import { MenuModule } from 'primeng/menu';
import { AvatarModule } from 'primeng/avatar';

import { LayoutService } from '@layout/services/layout.service';
import { AuthService } from '@features/auth/services/auth.service';
import {AppMenuModel} from "@shared/models/app-menu.model";
import {LayoutMenuService} from "@layout/services/layout-menu.service";

@Component({
    selector: 'app-topbar',
    standalone: true,
    imports: [
        CommonModule,
        StyleClassModule,
        MenuModule,
        AvatarModule
    ],
    templateUrl: './topbar.component.html',
    styleUrls: ['./topbar.component.scss']
})
export class TopbarComponent {

    menus: AppMenuModel[] = [];
    activeMenu!: AppMenuModel;
    userMenuItems: MenuItem[] = [];
    username = 'Waseem'; // later: load from token / user service
    loggingOut = false;

    private router = inject(Router);
    private authService = inject(AuthService);
    private menuService = inject(LayoutMenuService);

    constructor(public csmLayoutService: LayoutService) {}

    ngOnInit() {
        this.buildUserMenu();

        // ✅ load menus
        this.menuService.getMenu().subscribe(menu => {
            this.menus = menu;

            if (menu.length && !this.activeMenu) {
                this.selectMenu(menu[0]);
            }
        });
    }

    selectMenu(menu: AppMenuModel) {
        this.activeMenu = menu;
        this.menuService.setSelectedRoot(menu);
    }

    private buildUserMenu() {
        this.userMenuItems = [
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
                icon: this.loggingOut ? 'pi pi-spin pi-spinner' : 'pi pi-sign-out',
                command: () => this.logout()
            }
        ];
    }

    logout() {
        this.authService.logout().subscribe({
            next: () => {
                this.clearSession();
                this.router.navigate(['/auth/login']);
            },
            error: () => {
                this.clearSession();
                this.router.navigate(['/auth/login']);
            }
        });
    }

    private clearSession() {
        localStorage.removeItem('access_token');
        localStorage.removeItem('refresh_token');
    }


    toggleDarkMode() {
        this.csmLayoutService.layoutConfig.update((state) => ({
            ...state,
            darkTheme: !state.darkTheme
        }));
    }
}
