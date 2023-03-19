import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginCredentials } from 'src/app/models/login-credentials';
import { AuthService } from 'src/app/services/auth.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styles: ['#loginForm { max-width: 500px; }']
})
export class LoginComponent {
    protected isLoading = false;
    protected isAuthFailure = false;

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
        this.isAuthFailure = false;
        this.authService
            .login(this.loginForm.value as LoginCredentials)
            .subscribe((success) => {
                this.isLoading = false;
                if (success) {
                    this.router.navigate(['']);
                } else {
                    this.isAuthFailure = true;
                    this.loginForm.markAsPristine();
                }
            });
    }
}
