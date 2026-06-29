import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-form-section',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './app-form-section.component.html',
    styleUrls: ['./app-form-section.component.scss']
})
export class AppFormSectionComponent {
    @Input() title = '';
}
