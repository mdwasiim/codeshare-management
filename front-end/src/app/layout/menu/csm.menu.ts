import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CSMMenuitem } from '../menu-item/csm.menuitem';
import { CSMMenuItemModel } from '@/core/models/csm-menu.model';
import { CSMMenuService } from '@/core/services/csm-menu.service';

@Component({
    selector: 'csm-menu',
    standalone: true,
    imports: [CSMMenuitem, RouterModule],
    templateUrl: './csm.menu.html',
    styleUrls: ['./csm.menu.scss']
})
export class CSMMenu {
  model: CSMMenuItemModel[] = [];

  constructor(private csmMenuservice: CSMMenuService) {}

  ngOnInit() {
    this.csmMenuservice.getMenu().subscribe({
      next: (menu) => (this.model = menu),
      error: (err) => console.error('Menu load failed', err)
    });
  }
}
