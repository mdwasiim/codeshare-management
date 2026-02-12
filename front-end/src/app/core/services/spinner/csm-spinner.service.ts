import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable({ providedIn: 'root' })
export class CsmSpinnerService {

  private counter = 0;
  private loadingSubject = new BehaviorSubject<boolean>(false);

  loading$ = this.loadingSubject.asObservable();

  show(): void {
    this.counter++;
    if (this.counter === 1) {
      this.loadingSubject.next(true);
    }
  }

  hide(): void {
    this.counter--;
    if (this.counter <= 0) {
      this.counter = 0;
      this.loadingSubject.next(false);
    }
  }
}

