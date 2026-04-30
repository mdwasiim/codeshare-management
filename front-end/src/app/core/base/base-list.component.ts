import {Directive, OnInit} from '@angular/core';
import { Observable } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { CSMBasePageComponent } from './csm-base-page.component';

@Directive()
export abstract class BaseListComponent<T> extends CSMBasePageComponent  implements OnInit {

    data: T[] = [];

    // ✅ Strong typing
    abstract fetch(): Observable<T[]>;

    ngOnInit(): void {
        this.loadData();
    }

    loadData() {
        this.loading = true;

        this.fetch()
            .pipe(takeUntil(this.destroy$)) // ✅ prevent memory leaks
            .subscribe({
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
