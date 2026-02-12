import { Component, computed, inject, input } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { StyleClassModule } from 'primeng/styleclass';
import { CSMConfigurator } from '@layout/configurator/csm.configurator';  // UPDATED IMPORT
import { CommonModule } from '@angular/common';
import { CSMLayoutService } from '@/layout/menu/service/csm.layout.service';

@Component({
    selector: 'csm-floating-configurator',
    standalone: true,
    imports: [CommonModule, ButtonModule, StyleClassModule, CSMConfigurator],
    templateUrl: './csm.floating-configurator.html',
    styleUrls: ['./csm.floating-configurator.scss']
})
export class CSMFloatingConfigurator {
    csmLayoutService = inject(CSMLayoutService);

    float = input<boolean>(true);

    isDarkTheme = computed(() => this.csmLayoutService.layoutConfig().darkTheme);

    toggleDarkMode() {
        this.csmLayoutService.layoutConfig.update((state) => ({ ...state, darkTheme: !state.darkTheme }));
    }
}
