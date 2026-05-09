import {
    Component,
    Input
} from '@angular/core';

import {
    CommonModule
} from '@angular/common';

import {
    CardModule
} from 'primeng/card';

@Component({
    selector: 'csm-assignment-layout',
    standalone: true,
    imports: [
        CommonModule,
        CardModule
    ],
    templateUrl:
        './csm-assignment-layout.component.html'
})
export class CsmAssignmentLayoutComponent {

    @Input()
    groupTitle = 'Groups';

    @Input()
    title = 'Manage Assignment';

    @Input()
    leftTitle = '';

    @Input()
    rightTitle = '';
}
