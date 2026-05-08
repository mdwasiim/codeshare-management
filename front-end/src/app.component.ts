import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ToastModule} from "primeng/toast";
import {AuthInitializerService} from "@services/auth/auth-initializer.service";

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [
        RouterModule,
        ConfirmDialogModule,
        ToastModule
    ],
    templateUrl: './app.component.html'
})
export class AppComponent {

    constructor(
        private authInitializer:
        AuthInitializerService
    ) {

        this.authInitializer.init();
    }

}
