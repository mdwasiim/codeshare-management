import {
    Directive,
    inject,
    OnDestroy,
    OnInit
} from '@angular/core';

import {
    Observable,
    Subject
} from 'rxjs';

import {
    takeUntil
} from 'rxjs/operators';

import {
    PermissionService
} from '@core/security/permission.service';

@Directive()
export abstract class BaseListComponent<T>
    implements OnInit, OnDestroy {

    // =========================
    // DEPENDENCIES
    // =========================
    protected permissionService =
        inject(PermissionService);

    // =========================
    // RBAC
    // =========================
    protected resourceName?: string;

    // =========================
    // STATE
    // =========================
    data: T[] = [];

    loading = false;

    totalRecords = 0;

    page = 0;

    size = 10;

    // =========================
    // RXJS
    // =========================
    protected readonly destroy$ =
        new Subject<void>();

    // =========================
    // ABSTRACT API
    // =========================
    abstract fetch(): Observable<T[]>;

    // =========================
    // LIFECYCLE
    // =========================
    ngOnInit(): void {

        this.loadData();
    }

    ngOnDestroy(): void {

        this.destroy$.next();

        this.destroy$.complete();
    }

    // =========================
    // RBAC HELPERS
    // =========================
    can(action: string): boolean {

        if (!this.resourceName) {
            return true;
        }

        return this.permissionService.hasPermission(
            this.resourceName.toLowerCase(),
            action.toLowerCase()
        );
    }

    // =========================
    // LIST LOGIC
    // =========================
    loadData(): void {

        // READ permission required
        if (!this.can('READ')) {
            return;
        }

        this.handleObservable(
            this.fetch(),
            (res) => {

                this.data = res ?? [];

                this.afterLoad();
            }
        );
    }

    refresh(): void {

        this.loadData();
    }

    // =========================
    // CORE OBSERVABLE HANDLER
    // =========================
    protected handleObservable<R>(
        obs$: Observable<R>,
        onSuccess: (value: R) => void,
        onError?: (err: any) => void
    ): void {

        this.startLoading();

        obs$
            .pipe(
                takeUntil(this.destroy$)
            )
            .subscribe({

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

    // =========================
    // HOOKS
    // =========================
    protected afterLoad(): void {}

    protected onError(
        err: any
    ): void {

        console.error(err);
    }

    // =========================
    // LOADING HELPERS
    // =========================
    protected startLoading(): void {

        this.loading = true;
    }

    protected stopLoading(): void {

        this.loading = false;
    }
}
