import { Component } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { StyleClassModule } from 'primeng/styleclass';
import { CsmConfigurator } from '../configurator/csm.configurator';
import { CsmLayoutService } from '@/core/services/csm.layout.service';

@Component({
    selector: 'csm-topbar',
    standalone: true,
    imports: [RouterModule, CommonModule, StyleClassModule, CsmConfigurator],
    templateUrl: './csm.topbar.html',
    styleUrls: ['./csm.topbar.scss']
})
export class CsmTopbar {
    items!: MenuItem[];

    constructor(public csmLayoutService: CsmLayoutService) {}

    toggleDarkMode() {
        this.csmLayoutService.layoutConfig.update((state) => ({
            ...state,
            darkTheme: !state.darkTheme
        }));
    }
}
