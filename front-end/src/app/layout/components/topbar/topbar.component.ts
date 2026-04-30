import { Component, inject, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { StyleClassModule } from 'primeng/styleclass';
import { MenuModule } from 'primeng/menu';
import { AvatarModule } from 'primeng/avatar';

import { LayoutService } from '@layout/services/layout.service';
import { AuthService } from '@features/auth/services/auth.service';
import { AppMenuModel } from '@shared/models/app-menu.model';
import { LayoutMenuService } from '@layout/services/layout-menu.service';
import { AppTokenService } from '@services/auth/app-token.service';

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
export class TopbarComponent implements OnInit {
    userMenuItems: MenuItem[] = [];
    loggingOut = false;

    username = '';

    private router = inject(Router);
    private authService = inject(AuthService);
    private menuService = inject(LayoutMenuService);
    private tokenService = inject(AppTokenService);


    rootMenus$ = this.menuService.getRootMenus();
    selectedRoot$ = this.menuService.selectedRootMenu$;


    constructor(public csmLayoutService: LayoutService) {}

    ngOnInit() {
        this.username = this.tokenService.username || 'User';
        this.buildUserMenu();
    }

    selectMenu(menu: AppMenuModel) {
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
                icon: 'pi pi-sign-out',
                command: () => this.logout()
            }
        ];
    }

    logout() {
        if (this.loggingOut) return;

        this.loggingOut = true;

        this.authService.logout().subscribe({
            next: () => {
                this.loggingOut = false;
                this.router.navigate(['/auth/login']);
            },
            error: () => {
                this.loggingOut = false;
                this.router.navigate(['/auth/login']);
            }
        });
    }

    toggleDarkMode() {
        this.csmLayoutService.layoutConfig.update(state => ({
            ...state,
            darkTheme: !state.darkTheme
        }));
    }
}
