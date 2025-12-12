import { Component } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { StyleClassModule } from 'primeng/styleclass';
import { CSMConfigurator } from '../configurator/csm.configurator';
import { CSMLayoutService } from '@/core/services/csm.layout.service';

@Component({
    selector: 'csm-topbar',
    standalone: true,
    imports: [RouterModule, CommonModule, StyleClassModule, CSMConfigurator],
    templateUrl: './csm.topbar.html',
    styleUrls: ['./csm.topbar.scss']
})
export class CSMTopbar {
    items!: MenuItem[];

    constructor(public csmLayoutService: CSMLayoutService) {}

    toggleDarkMode() {
        this.csmLayoutService.layoutConfig.update((state) => ({
            ...state,
            darkTheme: !state.darkTheme
        }));
    }
}
