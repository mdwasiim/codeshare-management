import { Injectable, effect, signal, computed } from '@angular/core';
import { LayoutConfig } from '@layout/model/layout-config.model';
import { LayoutState } from '@layout/model/layout-state.model';

@Injectable({ providedIn: 'root' })
export class LayoutService {
    layoutConfig = signal<LayoutConfig>({
        preset: 'Aura',
        primary: 'emerald',
        surface: null,
        darkTheme: false,
        menuMode: 'static'
    });

    layoutState = signal<LayoutState>({
        staticMenuDesktopInactive: false,
        overlayMenuActive: false,
        mobileMenuActive: false
    });

    // =========================
    // COMPUTED
    // =========================

    isDarkTheme = computed(() => this.layoutConfig().darkTheme);

    isOverlay = computed(() => this.layoutConfig().menuMode === 'overlay');

    isSidebarActive = computed(() => this.layoutState().overlayMenuActive || this.layoutState().mobileMenuActive);

    // =========================
    // INIT
    // =========================

    constructor() {
        const saved = localStorage.getItem('theme');

        if (saved === 'dark') {
            this.layoutConfig.update((cfg) => ({ ...cfg, darkTheme: true }));
        }

        effect(() => {
            this.applyTheme(this.layoutConfig().darkTheme);
        });
    }

    // =========================
    // THEME
    // =========================

    toggleTheme() {
        this.layoutConfig.update((cfg) => ({
            ...cfg,
            darkTheme: !cfg.darkTheme
        }));

        localStorage.setItem('theme', this.layoutConfig().darkTheme ? 'dark' : 'light');
    }

    private applyTheme(isDark: boolean) {
        if (typeof document === 'undefined') return;

        document.documentElement.classList.toggle('app-dark', isDark);
    }

    // =========================
    // MENU / SIDEBAR
    // =========================

    onMenuToggle() {
        this.layoutState.update((prev) => {
            if (this.isOverlay()) {
                return { ...prev, overlayMenuActive: !prev.overlayMenuActive };
            }

            if (this.isDesktop()) {
                return { ...prev, staticMenuDesktopInactive: !prev.staticMenuDesktopInactive };
            }

            return { ...prev, mobileMenuActive: !prev.mobileMenuActive };
        });
    }

    closeSidebar() {
        this.layoutState.update((prev) => ({
            ...prev,
            overlayMenuActive: false,
            mobileMenuActive: false
        }));
    }

    // =========================
    // DEVICE
    // =========================

    isDesktop(): boolean {
        return typeof window !== 'undefined' && window.innerWidth > 991;
    }
}
