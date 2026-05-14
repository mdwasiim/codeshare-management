import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';

import { AuthInitializerService } from '@services/auth/auth-initializer.service';

import { CsmSpinnerOverlayComponent } from '@shared/components/spinner-overlay/csm-spinner-overlay.component';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, ConfirmDialogModule, ToastModule, CsmSpinnerOverlayComponent],
    templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
    constructor(private authInitializer: AuthInitializerService) {}

    ngOnInit(): void {
        this.authInitializer.init();
    }
}
