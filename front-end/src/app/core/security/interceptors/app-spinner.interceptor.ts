import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs';

import { CsmSpinnerService } from '@services/spinner/csm-spinner.service';

export const AppSpinnerInterceptor: HttpInterceptorFn = (req, next) => {
    const spinner = inject(CsmSpinnerService);

    spinner.show();

    return next(req).pipe(finalize(() => spinner.hide()));
};
