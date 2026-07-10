import { Component, OnInit, inject } from '@angular/core';
import { StatsComponent } from '@features/home/dashboard/components/stats/stats.component';
import { RecentSalesWidget } from '@features/home/dashboard/components/recent-sales/recent-sales.component';
import { RevenueStreamWidget } from '@features/home/dashboard/components/revenue-stream/revenue-stream.component';
import { NotificationsComponent } from '@features/home/dashboard/components/notifications/notifications.component';
import { BestSellingComponent } from '@features/home/dashboard/components/best-selling/best-selling.component';

@Component({
    selector: 'dashboard-page',
    standalone: true,
    templateUrl: './dashboard.page.html',
    styleUrls: ['./dashboard.page.scss'],
    imports: [StatsComponent, RecentSalesWidget, BestSellingComponent, RevenueStreamWidget, NotificationsComponent]
})
export class DashboardPage implements OnInit {
    ngOnInit(): void {
    }
}
