import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'csm-form-section',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './csm-form-section.component.html',
    styleUrls: ['./csm-form-section.component.scss']
})
export class CsmFormSectionComponent {
    @Input() title = '';
}
