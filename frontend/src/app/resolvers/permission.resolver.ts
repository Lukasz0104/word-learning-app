import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, ResolveFn, Router } from '@angular/router';
import { EMPTY, mergeMap, of } from 'rxjs';
import { SetPermissions } from '../models/set-permissions';
import { LearningSetService } from '../services/learning-set.service';

export const permissionResolver: ResolveFn<SetPermissions> = (
    route: ActivatedRouteSnapshot
) => {
    const id = route.paramMap.get('id');

    if (!id) {
        inject(Router).navigate(['']);
        return EMPTY;
    }

    const router = inject(Router);

    return inject(LearningSetService)
        .fetchPermissions(+id)
        .pipe(
            mergeMap((permissions) => {
                if (!permissions.ableToRead) {
                    router.navigate(['']);
                    return EMPTY;
                }

                return of(permissions);
            })
        );
};
