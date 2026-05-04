import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'csm-form-section',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './csm-form-section.component.html'
})
export class CsmFormSectionComponent {
    @Input() title = '';
}
