import { Component } from '@angular/core';
import { NotificationsWidget } from './components/notificationswidget';
import { StatsWidget } from './components/statswidget';
import { CSMRecentSalesWidget } from './components/recentsaleswidget';
import { BestSellingWidget } from './components/bestsellingwidget';
import { CSMRevenueStreamWidget } from './components/revenuestreamwidget';

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
export class CSMDashboard {}
