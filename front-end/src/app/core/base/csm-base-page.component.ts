import { Directive, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';

@Directive()
export abstract class CSMBasePageComponent implements OnDestroy {

  loading = true;

  protected readonly destroy$ = new Subject<void>();

  protected startLoading() {
    this.loading = true;
  }

  protected stopLoading() {
    this.loading = false;
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
