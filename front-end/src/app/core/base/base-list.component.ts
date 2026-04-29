import { OnInit } from '@angular/core';

export abstract class BaseListComponent<T> {

    data: T[] = [];
    loading = false;

    abstract fetch(): any;

    loadData() {
        this.loading = true;

        this.fetch().subscribe({
            next: (res: T[]) => {
                this.data = res ?? [];
                this.loading = false;
            },
            error: (err: any) => {
                console.error(err);
                this.loading = false;
            }
        });
    }

    refresh() {
        this.loadData();
    }
}
