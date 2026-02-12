import { Component, inject } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { StyleClassModule } from 'primeng/styleclass';
import { MenuModule } from 'primeng/menu';
import { AvatarModule } from 'primeng/avatar';

import { CSMConfigurator } from '../configurator/csm.configurator';
import { CSMLayoutService } from '@/layout/menu/service/csm.layout.service';
import { AuthService } from '@services/auth/auth.service';

@Component({
  selector: 'csm-topbar',
  standalone: true,
  imports: [
    CommonModule,
    StyleClassModule,
    MenuModule,
    AvatarModule,
    CSMConfigurator
  ],
  templateUrl: './csm.topbar.html',
  styleUrls: ['./csm.topbar.scss']
})
export class CSMTopbar {

  userMenuItems: MenuItem[] = [];
  username = 'Waseem'; // later: load from token / user service
  loggingOut = false;

  private router = inject(Router);
  private authService = inject(AuthService);

  constructor(public csmLayoutService: CSMLayoutService) {}

  ngOnInit() {
    this.buildUserMenu();
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
    this.authService.logout();      // sync
    this.router.navigate(['/auth/login']);
}


  toggleDarkMode() {
    this.csmLayoutService.layoutConfig.update((state) => ({
      ...state,
      darkTheme: !state.darkTheme
    }));
  }
}
