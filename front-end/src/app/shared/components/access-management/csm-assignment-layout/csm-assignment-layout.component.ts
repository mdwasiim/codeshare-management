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
    groupTitle?: string;

    @Input()
    title = 'Manage Assignment';

    @Input()
    leftTitle?: string;

    @Input()
    rightTitle?: string;


}
