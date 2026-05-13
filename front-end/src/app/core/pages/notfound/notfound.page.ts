import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'csm-notfound',
    standalone: true,
    imports: [RouterModule, ButtonModule],
    templateUrl: './notfound.page.html'
})
export class CSMNotfound {}
