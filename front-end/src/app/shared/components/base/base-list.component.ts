import { Directive, inject, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subject, Subscription } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { PermissionService } from '@core/security/permission.service';

@Directive()
export abstract class BaseListComponent<T> implements OnInit, OnDestroy {
    protected permissionService = inject(PermissionService);
    protected resourceName?: string;
    protected readonly destroy$ = new Subject<void>();
    private filterReloadHandle: ReturnType<typeof setTimeout> | null = null;
    private activeRequest?: Subscription;

    data: T[] = [];
    loading = false;
    totalRecords = 0;
    page = 0;
    size = 10;
    exactFilters: Record<string, string> = {};

    abstract fetch(): Observable<T[]>;

    ngOnInit(): void {
        this.loadData();
    }

    ngOnDestroy(): void {
        if (this.filterReloadHandle) {
            clearTimeout(this.filterReloadHandle);
        }

        this.activeRequest?.unsubscribe();
        this.destroy$.next();
        this.destroy$.complete();
    }

    can(action: string): boolean {
        if (!this.resourceName) {
            return true;
        }

        return this.permissionService.hasPermission(this.resourceName.toLowerCase(), action.toLowerCase());
    }

    loadData(): void {
        this.clearPendingFilterReload();
        this.handleObservable(this.fetch(), (res) => {
            this.data = res ?? [];
            this.afterLoad();
        });
    }

    refresh(): void {
        this.scheduleFilterReload();
    }

    get hasExactFilters(): boolean {
        return Object.values(this.exactFilters).some((value) => !!value?.trim());
    }

    onExactFilterChange(field: string, value: string): void {
        const trimmedValue = value.trim();

        if (trimmedValue) {
            this.exactFilters = {
                ...this.exactFilters,
                [field]: trimmedValue
            };
        } else {
            const { [field]: removed, ...remainingFilters } = this.exactFilters;
            this.exactFilters = remainingFilters;
        }

        this.loadData();
    }

    clearExactFilters(): void {
        if (!this.hasExactFilters) {
            return;
        }

        this.exactFilters = {};
        this.loadData();
    }

    private scheduleFilterReload(): void {
        this.clearPendingFilterReload();

        this.filterReloadHandle = setTimeout(() => {
            this.filterReloadHandle = null;
            this.loadData();
        }, 250);
    }

    private clearPendingFilterReload(): void {
        if (!this.filterReloadHandle) {
            return;
        }

        clearTimeout(this.filterReloadHandle);
        this.filterReloadHandle = null;
    }

    protected handleObservable<R>(obs$: Observable<R>, onSuccess: (value: R) => void, onError?: (err: unknown) => void): void {
        this.startLoading();

        this.activeRequest?.unsubscribe();
        this.activeRequest = obs$.pipe(takeUntil(this.destroy$)).subscribe({
            next: (res) => {
                onSuccess(res);
                this.stopLoading();
            },
            error: (err) => {
                this.stopLoading();
                this.onError(err);
                onError?.(err);
            }
        });
    }

    protected afterLoad(): void {}

    protected onError(err: unknown): void {
        console.error(err);
    }

    protected startLoading(): void {
        this.loading = true;
    }

    protected stopLoading(): void {
        this.loading = false;
    }
}
