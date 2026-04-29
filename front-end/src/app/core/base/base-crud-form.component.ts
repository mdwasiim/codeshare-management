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

    abstract buildForm(): void;
    abstract patchForm(data: T): void;
    abstract fetchById(id: string): void;
    abstract create(payload: any): void;
    abstract update(id: string, payload: any): void;

    init() {
        this.buildForm();

        if (this.data) {
            this.isEdit = true;
            this.patchForm(this.data);
        } else if (this.id) {
            this.isEdit = true;
            this.fetchById(this.id);
        }
    }

    submit() {
        if (this.form.invalid) return;

        const payload = this.form.value;

        if (this.isEdit) {
            this.update(this.id!, payload);
        } else {
            this.create(payload);
        }
    }
}
