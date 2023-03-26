import { Injectable } from '@angular/core';
import {
    AbstractControl,
    AsyncValidator,
    ValidationErrors
} from '@angular/forms';
import {
    debounceTime,
    distinctUntilChanged,
    first,
    map,
    Observable,
    switchMap
} from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class EmailAvaliableValidator implements AsyncValidator {
    constructor(private authService: AuthService) {}

    validate(
        control: AbstractControl
    ): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> {
        return control.valueChanges.pipe(
            debounceTime(1000),
            distinctUntilChanged(),
            switchMap((email) => this.authService.checkEmailAddress(email)),
            map((avaliable) => (avaliable ? null : { emailTaken: true })),
            first()
        );
    }
}
