import { Component } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html'
})
export class RegisterComponent {
    protected registerForm = this.fb.group({
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
    });
    // TODO add custom validator checking if repeat password is the same as password

    constructor(private fb: FormBuilder, private authService: AuthService) {}

    onRegister() {
        console.log('onRegister called');
    }
}
