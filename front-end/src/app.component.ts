import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ToastModule} from "primeng/toast";

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
export class AppComponent {}
