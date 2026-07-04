import { Directive, inject, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { PermissionService } from '@core/security/permission.service';

@Directive()
export abstract class BaseListComponent<T> implements OnInit, OnDestroy {
    protected permissionService = inject(PermissionService);
    protected resourceName?: string;
    protected readonly destroy$ = new Subject<void>();

    data: T[] = [];
    loading = false;
    totalRecords = 0;
    page = 0;
    size = 10;

    abstract fetch(): Observable<T[]>;

    ngOnInit(): void {
        this.loadData();
    }

    ngOnDestroy(): void {
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
        this.handleObservable(this.fetch(), (res) => {
            this.data = res ?? [];
            this.afterLoad();
        });
    }

    refresh(): void {
        this.loadData();
    }

    protected handleObservable<R>(obs$: Observable<R>, onSuccess: (value: R) => void, onError?: (err: unknown) => void): void {
        this.startLoading();

        obs$.pipe(takeUntil(this.destroy$)).subscribe({
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
