import { Component, ElementRef } from '@angular/core';
import { CSMMenu } from '../menu/csm.menu';

@Component({
    selector: 'csm-sidebar',
    standalone: true,
    imports: [CSMMenu],
    templateUrl: './csm.sidebar.html',
    styleUrls: ['./csm.sidebar.scss']
})
export class CSMSidebar {
    constructor(public el: ElementRef) {}
}
