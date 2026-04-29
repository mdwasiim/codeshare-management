import { ActivatedRoute, Router } from '@angular/router';

export abstract class BaseFormComponent<T> {

    id?: string;
    isEdit = false;

    constructor(
        protected route: ActivatedRoute,
        protected router: Router
    ) {}

    // 🔥 ADD THIS METHOD
    init(): void {
        const id = this.route.snapshot.paramMap.get('id');

        if (id) {
            this.id = id;
            this.isEdit = true;
            this.load();   // 🔥 auto load for edit
        }
    }

    abstract load(): void;
    abstract submit(): void;

    navigateBack(url: string) {
        this.router.navigate([url]);
    }
}
