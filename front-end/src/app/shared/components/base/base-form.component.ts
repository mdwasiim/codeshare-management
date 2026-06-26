import { Directive, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Observable, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Directive()
export abstract class BaseCrudForm<T> implements OnInit, OnChanges, OnDestroy {
    @Input() data: T | null = null;
    @Input() id: string | null = null;

    @Output() saved = new EventEmitter<void>();
    @Output() cancelled = new EventEmitter<void>();

    form!: FormGroup;
    isEdit = false;
    loading = false;

    private initialized = false;
    private readonly destroy$ = new Subject<void>();

    abstract buildForm(): void;
    abstract patchForm(data: T): void;
    abstract fetchById(id: string): Observable<T>;
    abstract create(payload: unknown): Observable<unknown>;
    abstract update(id: string, payload: unknown): Observable<unknown>;

    ngOnInit(): void {
        this.init();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if ((changes['id'] || changes['data']) && this.initialized) {
            this.init();
        }
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    init(): void {
        if (!this.initialized) {
            this.buildForm();
            this.initialized = true;
        }

        this.loading = false;

        if (this.data != null) {
            this.isEdit = true;
            this.form.reset();
            this.patchForm(this.data);
        } else if (this.id) {
            this.isEdit = true;
            this.loading = true;
            this.form.reset();
            this.fetchById(this.id)
                .pipe(takeUntil(this.destroy$))
                .subscribe({
                    next: (res) => {
                        this.patchForm(res);
                        this.loading = false;
                    },
                    error: (err) => {
                        this.loading = false;
                        this.onError(err);
                    }
                });
        } else {
            this.isEdit = false;
            this.form.reset(this.getDefaultValues());
        }

        this.form.markAsPristine();
        this.form.markAsUntouched();
    }

    submit(): void {
        if (this.form.invalid || this.loading) {
            return;
        }

        this.loading = true;

        const payload = this.form.value;
        const request = this.isEdit ? this.update(this.id!, payload) : this.create(payload);

        request.pipe(takeUntil(this.destroy$)).subscribe({
            next: () => {
                this.loading = false;
                this.saved.emit();
            },
            error: (err) => {
                this.loading = false;
                this.onError(err);
            }
        });
    }

    cancel(): void {
        this.cancelled.emit();
    }

    protected getDefaultValues(): Record<string, unknown> {
        return {};
    }

    protected onError(err: unknown): void {
        console.error(err);
    }
}
