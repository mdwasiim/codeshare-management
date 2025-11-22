import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { NgScrollbar } from 'ngx-scrollbar';
import { INavData } from '@coreui/angular';
import { NavService } from '../../services/nav/nav.service';

import { IconDirective } from '@coreui/icons-angular';
import {
  ContainerComponent,
  ShadowOnScrollDirective,
  SidebarBrandComponent,
  SidebarComponent,
  SidebarFooterComponent,
  SidebarHeaderComponent,
  SidebarNavComponent,
  SidebarToggleDirective,
  SidebarTogglerDirective
} from '@coreui/angular';

import { DefaultFooterComponent, CSMHeaderComponent } from '.';

function isOverflown(element: HTMLElement) {
  return (
    element.scrollHeight > element.clientHeight ||
    element.scrollWidth > element.clientWidth
  );
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './csm-layout.component.html',
  styleUrls: ['./csm-layout.component.scss'],
  imports: [
    SidebarComponent,
    SidebarHeaderComponent,
    SidebarBrandComponent,
    SidebarNavComponent,
    SidebarFooterComponent,
    SidebarToggleDirective,
    SidebarTogglerDirective,
    ContainerComponent,
    DefaultFooterComponent,
    CSMHeaderComponent,
    IconDirective,
    NgScrollbar,
    RouterOutlet,
    RouterLink,
    ShadowOnScrollDirective
  ]
})
export class CSMLayoutComponent  implements OnInit {
  
  navItems?: INavData[];

  constructor(private readonly navService: NavService) {
    // Store the data as a const-like value
    //this.navItems = this.navService.fetchMenue();

  }

  ngOnInit(): void {
    this.loadMenu();
  }

  loadMenu(): void {
    this.navService.fetchMenue().subscribe({
      next: (data) => {
        this.navItems = data;
        console.log('Menu loaded:', data);
      },
      error: (err) => {
        console.error('Error loading menu', err);
      }
    });
  }
  
}
