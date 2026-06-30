import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';

import { forkJoin, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseListComponent } from '@shared/components/base/base-list.component';
import { ToolbarActionComponent } from '@shared/components/toolbar/toolbar-action.component';
import { AppDialogComponent } from '@shared/components/app-dialog/app-dialog.component';
import { HasPermissionDirective } from '@shared/directives/permission/has-permission.directive';
import { AppConfirmService } from '@services/app-confirm.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { LayoutMenuService } from '@layout/services/layout-menu.service';
import { AppMenuModel } from '@features/access-management/iam/models/app-menu.model';
import { MenuFormPage } from '@features/access-management/iam/menus/pages/menu-form/menu-form.page';
import { MenuManagementService } from '../../services/menu-management.service';

type MenuTreeNode = {
    key: string;
    data: AppMenuModel;
    children: MenuTreeNode[];
    expanded: boolean;
};

@Component({
    selector: 'menu-list',
    standalone: true,
    imports: [CommonModule, ButtonModule, TagModule, TooltipModule, ToolbarActionComponent, MenuFormPage, AppDialogComponent, HasPermissionDirective],
    templateUrl: './menu-list.page.html',
    styleUrl: './menu-list.page.scss'
})
export class MenuListPage extends BaseListComponent<MenuTreeNode> {
    protected override resourceName = 'menu';

    private service = inject(MenuManagementService);
    private toast = inject(AppToastService);
    private confirm = inject(AppConfirmService);
    private layoutMenuService = inject(LayoutMenuService);

    private originalTree: MenuTreeNode[] = [];

    selectedMenus: AppMenuModel[] = [];
    selectedMenu: AppMenuModel | null = null;
    dialogVisible = false;
    selectedId: string | null = null;
    searchText = '';

    fetch(): Observable<MenuTreeNode[]> {
        return this.service.getAll().pipe(
            map((res) => {
                const tree = this.buildTree(res);
                this.originalTree = tree;
                return tree;
            })
        );
    }

    protected override afterLoad(): void {
        if (!this.selectedMenu) {
            this.selectedMenu = this.findFirstMenu(this.data);
        }
    }

    openCreate(): void {
        this.selectedId = null;
        this.dialogVisible = true;
    }

    openEdit(menu: AppMenuModel | null = this.selectedMenu): void {
        if (!menu) return;

        this.selectedId = menu.id ?? null;
        this.dialogVisible = true;
    }

    deleteMenu(menu: AppMenuModel | null = this.selectedMenu): void {
        if (!menu?.id) return;

        this.confirm.delete(`Delete menu "${menu.label}"?`, () => {
            this.service.delete(menu.id!).subscribe({
                next: () => {
                    this.toast.success('Menu deleted successfully');
                    this.selectedMenu = null;
                    this.refresh();
                },
                error: () => {
                    this.toast.error('Failed to delete menu');
                }
            });
        });
    }

    deleteSelectedMenus(): void {
        if (!this.selectedMenus.length) return;

        this.confirm.delete('Delete selected menus?', () => {
            const requests = this.selectedMenus
                .filter((menu) => !!menu.id)
                .sort((a, b) => this.getNodeDepth(b.id!) - this.getNodeDepth(a.id!))
                .map((menu) => this.service.delete(menu.id!));

            forkJoin(requests).subscribe({
                next: () => {
                    this.toast.success('Menus deleted successfully');
                    this.selectedMenus = [];
                    this.selectedMenu = null;
                    this.refresh();
                },
                error: () => {
                    this.toast.error('Failed to delete menus');
                }
            });
        });
    }

    onSaved(): void {
        this.toast.success('Menu saved successfully');
        this.dialogVisible = false;
        this.refresh();
        this.layoutMenuService.loadMenus().subscribe();
    }

    onSearch(value: string): void {
        this.searchText = value.trim().toLowerCase();
        this.data = this.searchText ? this.filterTree(this.originalTree, this.searchText) : this.originalTree;
        this.selectedMenu = this.findSelectedInTree(this.data, this.selectedMenu?.id) ?? this.findFirstMenu(this.data);
    }

    selectMenu(menu: AppMenuModel): void {
        this.selectedMenu = menu;
    }

    toggleNode(node: MenuTreeNode, siblings: MenuTreeNode[]): void {
        const shouldExpand = !node.expanded;

        if (!shouldExpand) {
            this.collapseTree([node]);
            return;
        }

        siblings.filter((sibling) => sibling !== node).forEach((sibling) => this.collapseTree([sibling]));

        node.expanded = true;
    }

    toggleSelection(node: MenuTreeNode, checked: boolean): void {
        const subtreeMenus = this.flattenMenus([node]).filter((menu) => !!menu.id);
        const subtreeIds = new Set(subtreeMenus.map((menu) => menu.id));

        if (checked) {
            const selectedById = new Map(this.selectedMenus.filter((menu) => !!menu.id).map((menu) => [menu.id, menu]));
            subtreeMenus.forEach((menu) => selectedById.set(menu.id, menu));
            this.selectedMenus = Array.from(selectedById.values());
            return;
        }

        this.selectedMenus = this.selectedMenus.filter((item) => !subtreeIds.has(item.id));
    }

    isNodeSelected(node: MenuTreeNode): boolean {
        const subtreeMenus = this.flattenMenus([node]).filter((menu) => !!menu.id);
        return !!subtreeMenus.length && subtreeMenus.every((menu) => this.isMenuSelected(menu));
    }

    isNodePartiallySelected(node: MenuTreeNode): boolean {
        const subtreeMenus = this.flattenMenus([node]).filter((menu) => !!menu.id);
        const selectedCount = subtreeMenus.filter((menu) => this.isMenuSelected(menu)).length;

        return selectedCount > 0 && selectedCount < subtreeMenus.length;
    }

    get totalMenus(): number {
        return this.countNodes(this.originalTree);
    }

    get selectedChildrenCount(): number {
        if (!this.selectedMenu?.id) return 0;

        const node = this.findNode(this.originalTree, this.selectedMenu.id);
        return node?.children.length ?? 0;
    }

    displayValue(value: unknown): string {
        if (value === undefined || value === null || value === '') {
            return '-';
        }

        return String(value);
    }

    displayDate(value?: string | null): string {
        if (!value) return '-';

        const date = new Date(value);
        return Number.isNaN(date.getTime()) ? value : date.toLocaleString();
    }

    formatCode(value?: string | null): string {
        if (!value) return '-';

        return value
            .toLowerCase()
            .split(/[_\s-]+/)
            .filter(Boolean)
            .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
            .join(' ');
    }

    getMenuSubtitle(menu: AppMenuModel): string {
        return menu.route || this.formatCode(menu.code);
    }

    getChildCountLabel(count: number): string {
        return `${count} ${count === 1 ? 'child item' : 'child items'}`;
    }

    getGroupSummary(menu: AppMenuModel): string {
        const count = menu.groupIds?.length ?? 0;

        if (!count) return '-';
        return `${count} ${count === 1 ? 'group' : 'groups'} assigned`;
    }

    getIconSummary(icon?: string | null): string {
        if (!icon) return '-';

        return icon
            .replace(/\bpi\b/g, '')
            .replace(/\bpi-fw\b/g, '')
            .replace(/\s+/g, ' ')
            .trim()
            .replace(/^pi-/, '')
            .split('-')
            .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
            .join(' ');
    }

    getActiveLabel(value?: boolean | null): string {
        if (value === true) return 'Active';
        if (value === false) return 'Inactive';
        return 'Not set';
    }

    getActiveSeverity(value?: boolean | null): 'success' | 'secondary' | 'info' {
        if (value === true) return 'success';
        if (value === false) return 'secondary';
        return 'info';
    }

    exportCSV(): void {
        const rows = this.flattenMenus(this.originalTree);
        const header = ['Label', 'Topbar Label', 'Sidebar Label', 'Code', 'Route', 'Icon', 'Parent Id', 'Order', 'Active'];
        const csv = [header, ...rows.map((menu) => [menu.label, menu.topbarLabel, menu.sidebarLabel, menu.code, menu.route, menu.icon, menu.parentId, menu.displayOrder, menu.active])].map((row) => row.map((cell) => `"${this.displayValue(cell).replace(/"/g, '""')}"`).join(',')).join('\n');
        const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = 'menus.csv';
        link.click();
        URL.revokeObjectURL(link.href);
    }

    private buildTree(items: AppMenuModel[]): MenuTreeNode[] {
        const map = new Map<string, MenuTreeNode>();

        items.forEach((item) => {
            if (!item?.id) return;

            map.set(item.id, {
                key: item.id,
                data: { ...item },
                children: [],
                expanded: false
            });
        });

        const roots: MenuTreeNode[] = [];

        items.forEach((item) => {
            if (!item?.id) return;

            const node = map.get(item.id);
            if (!node) return;

            if (item.parentId && map.has(item.parentId)) {
                map.get(item.parentId)!.children.push(node);
            } else {
                roots.push(node);
            }
        });

        const sortTree = (nodes: MenuTreeNode[]) => {
            nodes.sort((a, b) => (a.data.displayOrder ?? 0) - (b.data.displayOrder ?? 0));
            nodes.forEach((node) => sortTree(node.children));
        };

        sortTree(roots);
        return roots;
    }

    private filterTree(nodes: MenuTreeNode[], query: string): MenuTreeNode[] {
        return nodes
            .map((node) => {
                const children = this.filterTree(node.children, query);
                const menu = node.data;
                const selfMatches = [menu.label, menu.topbarLabel, menu.sidebarLabel, menu.code, menu.route, menu.icon].some((value) => value?.toLowerCase().includes(query));

                if (!selfMatches && !children.length) {
                    return null;
                }

                return {
                    ...node,
                    expanded: true,
                    children
                };
            })
            .filter((node): node is MenuTreeNode => !!node);
    }

    private findFirstMenu(nodes: MenuTreeNode[]): AppMenuModel | null {
        return nodes[0]?.data ?? null;
    }

    private findSelectedInTree(nodes: MenuTreeNode[], id?: string): AppMenuModel | null {
        if (!id) return null;

        return this.findNode(nodes, id)?.data ?? null;
    }

    private findNode(nodes: MenuTreeNode[], id: string): MenuTreeNode | null {
        for (const node of nodes) {
            if (node.data.id === id) return node;

            const child = this.findNode(node.children, id);
            if (child) return child;
        }

        return null;
    }

    private getNodeDepth(id: string): number {
        const visit = (nodes: MenuTreeNode[], depth: number): number => {
            for (const node of nodes) {
                if (node.data.id === id) return depth;

                const childDepth = visit(node.children, depth + 1);
                if (childDepth >= 0) return childDepth;
            }

            return -1;
        };

        return visit(this.originalTree, 0);
    }

    private countNodes(nodes: MenuTreeNode[]): number {
        return nodes.reduce((total, node) => total + 1 + this.countNodes(node.children), 0);
    }

    private collapseTree(nodes: MenuTreeNode[]): void {
        nodes.forEach((node) => {
            node.expanded = false;
            this.collapseTree(node.children);
        });
    }

    private isMenuSelected(menu: AppMenuModel): boolean {
        return !!menu.id && this.selectedMenus.some((item) => item.id === menu.id);
    }

    private flattenMenus(nodes: MenuTreeNode[]): AppMenuModel[] {
        return nodes.flatMap((node) => [node.data, ...this.flattenMenus(node.children)]);
    }
}
