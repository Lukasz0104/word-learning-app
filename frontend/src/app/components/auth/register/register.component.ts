import { Component } from '@angular/core';
import {
    AbstractControl,
    FormControl,
    FormGroup,
    ValidationErrors,
    ValidatorFn,
    Validators
} from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';

const passwordMatchingValidator: ValidatorFn = (
    control: AbstractControl
): ValidationErrors | null => {
    const password = control.get('password');
    const repeatPassword = control.get('repeatPassword');

    return password && repeatPassword && password.value === repeatPassword.value
        ? null
        : { passwordMismatch: true };
};

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html'
})
export class RegisterComponent {
    protected registerForm = new FormGroup(
        {
            username: new FormControl('', [Validators.required]),
            password: new FormControl('', [
                Validators.required,
                Validators.minLength(8)
            ]),
            repeatPassword: new FormControl('', [
                Validators.required,
                Validators.minLength(8)
            ]),
            email: new FormControl('', [Validators.required, Validators.email])
        },
        { validators: passwordMatchingValidator }
    );

    constructor(private authService: AuthService) {}

    onRegister() {
        console.log('onRegister called');
    }

    protected get usernameControl() {
        return this.registerForm.controls.username;
    }

    protected get passwordControl() {
        return this.registerForm.controls.password;
    }

    protected get repeatPasswordControl() {
        return this.registerForm.controls.repeatPassword;
    }

    protected get emailControl() {
        return this.registerForm.controls.email;
    }

    protected get passwordMismatchError() {
        return this.registerForm.errors?.['passwordMismatch'];
    }
}
