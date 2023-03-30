import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { EmailAvaliableValidator } from 'src/app/validators/email-avaliable.validator';
import { repeatPasswordValidator } from 'src/app/validators/repeat-password.validator';
import { UsernameAvaliableValidator } from 'src/app/validators/username-avaliable.validator';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html'
})
export class RegisterComponent {
    protected isLoading = false;

    /**
     * Represents current registration status.
     *
     * @remarks
     * `true` - registration was successful,
     *
     * `false` - registration failed,
     *
     * `null` - registration was not processed yet.
     */
    protected isSuccess: boolean | null = null;

    protected registrationFailureReason = '';

    protected registerForm = new FormGroup(
        {
            username: new FormControl('', {
                nonNullable: true,
                validators: [Validators.required, Validators.maxLength(20)],
                asyncValidators: [
                    this.usernameValidator.validate.bind(this.usernameValidator)
                ]
            }),
            password: new FormControl('', {
                nonNullable: true,
                validators: [Validators.required, Validators.minLength(8)]
            }),
            repeatPassword: new FormControl('', {
                nonNullable: true,
                validators: [Validators.required, Validators.minLength(8)]
            }),
            emailAddress: new FormControl('', {
                nonNullable: true,
                validators: [Validators.required, Validators.email],
                asyncValidators: [
                    this.emailValidator.validate.bind(this.emailValidator)
                ]
            })
        },
        { validators: repeatPasswordValidator }
    );

    constructor(
        private authService: AuthService,
        private usernameValidator: UsernameAvaliableValidator,
        private emailValidator: EmailAvaliableValidator
    ) {}

    onRegister() {
        const { repeatPassword, ...dto } = this.registerForm.getRawValue();

        this.isLoading = true;
        this.isSuccess = null;

        this.authService.register(dto).subscribe((reason) => {
            this.isSuccess = !reason;
            this.registrationFailureReason = reason;
            this.isLoading = false;

            if (this.isSuccess) {
                this.registerForm.reset();
            }
        });
    }

    //#region Form controls
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
        return this.registerForm.controls.emailAddress;
    }

    protected get passwordMismatchError() {
        return this.registerForm.errors?.['passwordMismatch'];
    }
    //#endregion
}
