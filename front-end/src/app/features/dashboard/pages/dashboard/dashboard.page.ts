import { Component, OnInit, inject } from '@angular/core';

import { MenuService } from '@shared/services/menu.service';
import {StatsWidgetComponent} from "@features/dashboard/components/stats-widget/stats-widget.component";
import {RecentSalesWidget} from "@features/dashboard/components/recent-sales-widget/recent-sales-widget.component";
import {RevenueStreamWidget} from "@features/dashboard/components/revenue-stream-widget/revenue-stream-widget.component";
import {NotificationsWidgetComponent} from "@features/dashboard/components/notifications-widget/notifications-widget.component";
import {BestSellingWidgetComponent} from "@features/dashboard/components/best-selling-widget/best-selling-widget.component";

@Component({
    selector: 'dashboard-widget',
    standalone: true,
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    imports: [
        StatsWidgetComponent,
        RecentSalesWidget,
        BestSellingWidgetComponent,
        RevenueStreamWidget,
        NotificationsWidgetComponent
    ]
})
export class DashboardComponent implements OnInit {

    private menuService = inject(MenuService);

    ngOnInit(): void {
        this.menuService.loadMenus().subscribe({
            error: err => console.error('Failed to load menus', err)
        });
    }
}
