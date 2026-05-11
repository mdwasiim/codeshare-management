import {AppMenuModel} from "@features/access-management/iam/models/app-menu.model";
import {Group} from "@features/access-management/iam/models/group.model";
import {AppToastService} from "@services/app-toast.service";
import {Component, inject, OnInit} from "@angular/core";
import {GroupMenuService} from "@features/access-management/assignment/group-menus/services/group-menu.service";
import {forkJoin} from "rxjs";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {AccordionModule} from "primeng/accordion";
import {CheckboxModule} from "primeng/checkbox";
import {TableModule} from "primeng/table";
import {
    CsmAssignmentLayoutComponent
} from "@shared/components/access-management/csm-assignment-layout/csm-assignment-layout.component";
import {ToolbarActionComponent} from "@shared/components/toolbar/toolbar-action.component";
import {TreeNode} from "primeng/api";
import {Tree} from "primeng/tree";
import {LayoutMenuService} from "@layout/services/layout-menu.service";

@Component({
    selector: 'app-group-menu',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,

        AccordionModule,
        CheckboxModule,
        TableModule,

        CsmAssignmentLayoutComponent,
        ToolbarActionComponent,
        Tree
    ],
    templateUrl: './group-menu.component.html'
})
export class GroupMenuComponent
    implements OnInit {

    private service =
        inject(GroupMenuService);

    private toast =
        inject(AppToastService);

    private layoutMenuService =
        inject(LayoutMenuService);

    // =====================================================
    // STATE
    // =====================================================
    groups: Group[] = [];

    menus: AppMenuModel[] = [];
    menuTree: TreeNode[] = [];

    selectedMenus: TreeNode[] = [];

    selectedGroup?: Group;

    loading = false;

    saving = false;

    // =====================================================
    // INIT
    // =====================================================
    ngOnInit(): void {

        this.loadData();
    }

    // =====================================================
    // LOAD DATA
    // =====================================================
    loadData(): void {

        this.loading = true;

        forkJoin({

            groups:
                this.service.getAllGroups(),

            menus:
                this.service.getAllMenus()

        }).subscribe({

            next: (res) => {

                this.groups =
                    res.groups || [];

                this.menus =
                    res.menus || [];

                this.menuTree =
                    this.layoutMenuService
                        .buildTree(this.menus);

                this.loading = false;
            },

            error: (err) => {

                console.error(err);

                this.loading = false;

                this.toast.error(
                    'Failed to load data'
                );
            }
        });
    }

    // =====================================================
    // GROUP SELECT
    // =====================================================
    onGroupSelect(
        group: Group
    ): void {

        this.selectedGroup = group;

        this.selectedMenus = [];

        if (!group.id) {
            return;
        }

        this.loading = true;

        this.service
            .getMenusByGroup(group.id)
            .subscribe({

                next: (menus) => {

                    const ids =
                        menus
                            .map(menu => menu.id!)
                            .filter(Boolean);

                    const tree =
                        this.layoutMenuService
                            .buildTree(this.menus);

                    this.menuTree =
                        this.toTreeNodes(tree);

                    this.expandAll(this.menuTree);

                    this.selectedMenus =
                        this.findSelectedNodes(
                            this.menuTree,
                            ids
                        );

                    this.loading = false;
                },

                error: (err) => {

                    console.error(err);

                    this.loading = false;

                    this.toast.error(
                        'Failed to load menus'
                    );
                }
            });
    }

    toTreeNodes(
        menus: AppMenuModel[]
    ): TreeNode[] {

        return menus.map(menu => ({

            key: menu.id,

            label: menu.label,

            icon: menu.icon,

            data: menu,

            children: menu.items?.length
                ? this.toTreeNodes(menu.items)
                : []

        }));
    }

    // =====================================================
    // SAVE
    // =====================================================
    save(): void {

        if (!this.selectedGroup?.id) {

            this.toast.warn(
                'Please select a group'
            );

            return;
        }

        this.saving = true;

        this.service
            .replaceGroupMenus(
                this.selectedGroup.id,
                this.selectedMenus
                    .map(node => node.key!)
            )
            .subscribe({

                next: () => {

                    this.toast.success(
                        'Group menus updated successfully'
                    );

                    this.saving = false;
                },

                error: (err) => {

                    console.error(err);

                    this.toast.error(
                        'Failed to update menus'
                    );

                    this.saving = false;
                }
            });
    }
    expandAll(
        nodes: TreeNode[]
    ): void {

        nodes.forEach(node => {

            node.expanded = true;

            if (node.children?.length) {

                this.expandAll(node.children);
            }
        });
    }

    // =====================================================
    // RESET
    // =====================================================
    reset(): void {

        if (!this.selectedGroup) {
            return;
        }

        this.onGroupSelect(this.selectedGroup);
    }

    findSelectedNodes(
        nodes: TreeNode[],
        ids: string[]
    ): TreeNode[] {

        const selected: TreeNode[] = [];

        const traverse = (
            items: TreeNode[]
        ) => {

            items.forEach(node => {

                if (ids.includes(node.key!)) {

                    selected.push(node);
                }

                if (node.children?.length) {

                    traverse(node.children);
                }
            });
        };

        traverse(nodes);

        return selected;
    }
}
