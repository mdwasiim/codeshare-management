import { Component, OnInit,inject } from '@angular/core';
import { NotificationsWidget } from './notificationswidget';
import { StatsWidget } from './statswidget';
import { CSMRecentSalesWidget } from './recentsaleswidget';
import { BestSellingWidget } from './bestsellingwidget';
import { CSMRevenueStreamWidget } from './revenuestreamwidget';
import { CSMMenuService } from '@/layout/menu/service/csm-menu.service';


@Component({
    selector: 'csm-dashboard',
    imports: [StatsWidget, CSMRecentSalesWidget, BestSellingWidget, CSMRevenueStreamWidget, NotificationsWidget],
    template: `
        <div class="grid grid-cols-12 gap-8">
            <csm-stats-widget class="contents" />
            <div class="col-span-12 xl:col-span-6">
                <csm-recent-sales-widget />
                <csm-best-selling-widget />
            </div>
            <div class="col-span-12 xl:col-span-6">
                <csm-revenue-stream-widget />
                <csm-notifications-widget />
            </div>
        </div>
    `
})
export class CSMDashboard implements OnInit{

 private menuService = inject(CSMMenuService);

   ngOnInit(): void {
    // ✅ Load protected data AFTER login & navigation
    this.menuService.loadMenus().subscribe({
      error: err => console.error('Failed to load menus', err)
    });
  }

}
