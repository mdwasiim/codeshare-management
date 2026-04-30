import { Directive, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Directive()
export abstract class BaseCrudForm<T> {

    @Input() data: T | null = null;
    @Input() id: string | null = null;

    @Output() saved = new EventEmitter<void>();
    @Output() cancelled = new EventEmitter<void>();

    form!: FormGroup;
    isEdit = false;
    loading = false;

    private initialized = false; // ✅ prevent rebuild

    abstract buildForm(): void;
    abstract patchForm(data: T): void;
    abstract fetchById(id: string): any;
    abstract create(payload: any): any;
    abstract update(id: string, payload: any): any;

    // 🔥 SAFE INIT
    init() {

        // ✅ build form only once
        if (!this.initialized) {
            this.buildForm();
            this.initialized = true;
        }

        if (this.data) {
            this.isEdit = true;
            this.patchForm(this.data);

        } else if (this.id) {
            this.isEdit = true;
            this.fetchById(this.id).subscribe((res: T) => {
                this.patchForm(res);
            });

        } else {
            this.isEdit = false;
            this.form.reset(this.getDefaultValues()); // ✅ safe reset
        }
    }

    submit() {
        if (this.form.invalid) return;

        this.loading = true;

        const payload = this.form.value;

        const request = this.isEdit
            ? this.update(this.id!, payload)
            : this.create(payload);

        request.subscribe({
            next: () => {
                this.loading = false;
                this.saved.emit();
            },
            error: (err: any) => {
                console.error(err);
                this.loading = false;
            }
        });
    }

    // ✅ override if needed
    protected getDefaultValues(): any {
        return {};
    }
}
