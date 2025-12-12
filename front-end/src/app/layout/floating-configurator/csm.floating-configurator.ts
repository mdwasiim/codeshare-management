import { Component, computed, inject, input } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { StyleClassModule } from 'primeng/styleclass';
import { CsmConfigurator } from '../configurator/csm.configurator';  // UPDATED IMPORT
import { CommonModule } from '@angular/common';
import { CsmLayoutService } from '@/core/services/csm.layout.service';

@Component({
    selector: 'csm-floating-configurator',
    standalone: true,
    imports: [CommonModule, ButtonModule, StyleClassModule, CsmConfigurator],
    templateUrl: './csm.floating-configurator.html',
    styleUrls: ['./csm.floating-configurator.scss']
})
export class CsmFloatingConfigurator {
    csmLayoutService = inject(CsmLayoutService);

    float = input<boolean>(true);

    isDarkTheme = computed(() => this.csmLayoutService.layoutConfig().darkTheme);

    toggleDarkMode() {
        this.csmLayoutService.layoutConfig.update((state) => ({ ...state, darkTheme: !state.darkTheme }));
    }
}
