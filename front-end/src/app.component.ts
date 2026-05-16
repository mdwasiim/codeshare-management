import { Component, OnDestroy, OnInit } from '@angular/core';
import { NavigationCancel, NavigationEnd, NavigationError, NavigationStart, RouteConfigLoadEnd, RouteConfigLoadStart, Router, RouterOutlet } from '@angular/router';
import { Subscription } from 'rxjs';

import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';

import { AuthInitializerService } from '@services/auth/auth-initializer.service';

import { CsmSpinnerOverlayComponent } from '@shared/components/spinner-overlay/csm-spinner-overlay.component';
import { CsmSpinnerService } from '@services/spinner/csm-spinner.service';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, ConfirmDialogModule, ToastModule, CsmSpinnerOverlayComponent],
    templateUrl: './app.component.html'
})
export class AppComponent implements OnInit, OnDestroy {
    private routerEventsSub?: Subscription;

    constructor(
        private authInitializer: AuthInitializerService,
        private router: Router,
        private spinner: CsmSpinnerService
    ) {}

    ngOnInit(): void {
        this.authInitializer.init();
        this.bindRouteSpinner();
    }

    ngOnDestroy(): void {
        this.routerEventsSub?.unsubscribe();
    }

    private bindRouteSpinner(): void {
        this.routerEventsSub = this.router.events.subscribe((event) => {
            if (event instanceof NavigationStart || event instanceof RouteConfigLoadStart) {
                this.spinner.show();
                return;
            }

            if (event instanceof NavigationEnd || event instanceof NavigationCancel || event instanceof NavigationError || event instanceof RouteConfigLoadEnd) {
                this.spinner.hide();
            }
        });
    }
}
