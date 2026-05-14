import { Component, DestroyRef, OnInit, inject } from '@angular/core';
import { AppMenuItemComponent } from '@layout/components/sidebar-menu/app-menu-item/app-menu-item.component';
import { AppMenuModel } from '@features/access-management/iam/models/app-menu.model';
import { LayoutMenuService } from '@layout/services/layout-menu.service';
import { combineLatest, startWith } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
    selector: 'app-menu',
    standalone: true,
    imports: [AppMenuItemComponent],
    templateUrl: './app-menu.component.html',
    styleUrls: ['./app-menu.component.scss']
})
export class AppMenuComponent implements OnInit {
    private readonly destroyRef = inject(DestroyRef);

    model: AppMenuModel[] = [];

    constructor(private menuService: LayoutMenuService) {}

    rootTrackKey(item: AppMenuModel, index: number): string {
        return item.id ?? item.code ?? item.route ?? `${item.label}-${index}`;
    }

    ngOnInit(): void {
        this.menuService.loadMenus().pipe(takeUntilDestroyed(this.destroyRef)).subscribe();

        combineLatest([this.menuService.getMenu(), this.menuService.selectedRootMenu$.pipe(startWith(null))])
            .pipe(takeUntilDestroyed(this.destroyRef))
            .subscribe(([menu, selectedRoot]) => {
                if (!menu.length) {
                    this.model = [];
                    return;
                }

                const root = selectedRoot ?? menu[0];
                if (!selectedRoot) {
                    this.menuService.setSelectedRoot(root);
                }

                this.model = root.items ?? [];
            });
    }
}
