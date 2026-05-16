import { Injectable } from '@angular/core';
import { BehaviorSubject, from, Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AppSpinnerService {
    private pendingCount = 0;
    private readonly loadingSubject = new BehaviorSubject<boolean>(false);

    readonly loading$ = this.loadingSubject.asObservable();

    show(): void {
        this.pendingCount++;
        if (this.pendingCount === 1) {
            this.loadingSubject.next(true);
        }
    }

    hide(): void {
        this.pendingCount = Math.max(0, this.pendingCount - 1);
        if (this.pendingCount === 0) {
            this.loadingSubject.next(false);
        }
    }

    reset(): void {
        this.pendingCount = 0;
        this.loadingSubject.next(false);
    }

    track$<T>(source$: Observable<T>): Observable<T> {
        this.show();
        return source$.pipe(finalize(() => this.hide()));
    }

    trackPromise<T>(promise: Promise<T>): Promise<T> {
        this.show();
        return promise.finally(() => this.hide());
    }

    run$<T>(source$: Observable<T>): Observable<T> {
        return this.track$(source$);
    }

    runPromise<T>(promise: Promise<T>): Promise<T> {
        return this.trackPromise(promise);
    }

    run<T>(operation: Observable<T> | Promise<T>): Observable<T> {
        return this.track$(from(operation));
    }
}
