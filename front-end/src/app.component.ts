import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'csm-root',
    standalone: true,
    imports: [RouterModule],
    template: `<router-outlet></router-outlet>`
})
export class CSMComponent {}
