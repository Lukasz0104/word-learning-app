import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginCredentials } from 'src/app/models/login-credentials';
import { AuthService } from 'src/app/services/auth.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html'
})
export class LoginComponent {
    protected isLoading = false;

    protected loginForm = this.fb.group({
        username: ['', Validators.required],
        password: ['', Validators.required]
    });

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router
    ) {}

    onSubmit() {
        this.isLoading = true;
        this.authService
            .login(this.loginForm.value as LoginCredentials)
            .subscribe(() => {
                this.isLoading = false;
                this.router.navigate(['']);
            });
        // TODO display alert
    }
}
