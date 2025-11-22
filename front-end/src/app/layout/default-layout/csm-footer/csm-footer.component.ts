import { Component } from '@angular/core';
import { FooterComponent } from '@coreui/angular';

@Component({
  selector: 'app-default-footer',
  templateUrl: './csm-footer.component.html',
  styleUrls: ['./csm-footer.component.scss']
})
export class DefaultFooterComponent extends FooterComponent {
  constructor() {
    super();
  }
}
