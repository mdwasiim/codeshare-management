import {Directive, inject, OnDestroy, OnInit} from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import {CSMCrudPermissions} from "@core/models/app-permission.model";
import {AuthzService} from "@services/authz.service";

@Directive()
export abstract class BaseListComponent<T> implements OnInit, OnDestroy {

    // 🔹 ADD THESE TWO LINES
    protected authz = inject(AuthzService);
    protected resourceName?: string;

    data: T[] = [];
    loading = true;

    // ✅ RBAC support (optional for child classes)
    permissions: Partial<CSMCrudPermissions> = {};
    protected readonly destroy$ = new Subject<void>();

    // ========================
    // ABSTRACT API
    // ========================
    abstract fetch(): Observable<T[]>;

    // ========================
    // LIFECYCLE
    // ========================
    ngOnInit(): void {
        this.initPermissions();
        this.loadData();
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    // ========================
    // PERMISSION INIT (HOOK)
    // ========================
    protected initPermissions() {
        console.log('RESOURCE=', this.resourceName);

        console.log('PERMISSIONS=', this.permissions);
        // ✅ backward compatibility (no RBAC applied)
        if (!this.resourceName) {
            this.permissions = {
                canCreate: true,
                canDelete: true,
                canExport: true,
                canUpload: true,
                canRefresh: true
            };
            return;
        }

        const has = (action: string) =>
            this.authz.has(this.resourceName!, action);

        this.permissions = {
            canCreate: has('CREATE'),
            canDelete: has('DELETE'),
            canExport: has('EXPORT'),
            canUpload: has('UPLOAD'),
            canRefresh: has('READ')
        };
    }

    // ========================
    // PERMISSION CHECK HELPERS
    // ========================
    hasPermission(key: keyof CSMCrudPermissions): boolean {
        return !!this.permissions?.[key];
    }

    // ========================
    // CORE OBSERVABLE HANDLER
    // ========================
    protected handleObservable<R>(
        obs$: Observable<R>,
        onSuccess: (value: R) => void,
        onError?: (err: any) => void
    ) {
        this.startLoading();

        obs$
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (res) => {
                    onSuccess(res);
                    this.stopLoading();
                },
                error: (err) => {
                    console.error(err);
                    onError?.(err);
                    this.stopLoading();
                }
            });
    }

    // ========================
    // LIST LOGIC
    // ========================
    loadData() {
        // optional guard
        if (this.resourceName && !this.hasPermission('canRefresh')) {
            return;
        }

        this.handleObservable(
            this.fetch(),
            (res) => this.data = res ?? []
        );
    }

    refresh() {
        this.loadData();
    }

    // ========================
    // LOADING HELPERS
    // ========================
    protected startLoading() {
        this.loading = true;
    }

    protected stopLoading() {
        this.loading = false;
    }
}
