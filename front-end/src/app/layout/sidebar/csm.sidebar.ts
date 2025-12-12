import { Component, ElementRef } from '@angular/core';
import { CsmMenu } from '../menu/csm.menu';

@Component({
    selector: 'csm-sidebar',
    standalone: true,
    imports: [CsmMenu],
    templateUrl: './csm.sidebar.html',
    styleUrls: ['./csm.sidebar.scss']
})
export class CsmSidebar {
    constructor(public el: ElementRef) {}
}
