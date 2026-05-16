import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs';

import { AppSpinnerService } from '@services/spinner/app-spinner.service';

export const AppSpinnerInterceptor: HttpInterceptorFn = (req, next) => {
    const spinner = inject(AppSpinnerService);

    spinner.show();

    return next(req).pipe(finalize(() => spinner.hide()));
};
