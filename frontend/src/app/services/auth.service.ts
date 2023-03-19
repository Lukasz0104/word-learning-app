import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoginCredentials } from '../models/login-credentials';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private _isAuthenticated = false;
    private _token = '';

    get isAuthenticated() {
        return this._isAuthenticated;
    }

    get token() {
        return this._token;
    }

    constructor(private http: HttpClient, private router: Router) {}

    login(credentials: LoginCredentials) {
        return this.http
            .post<void>(`${environment.apiUrl}/login`, credentials, {
                observe: 'response'
            })
            .pipe(
                map((res) => {
                    const token = res.headers.get('Authorization');

                    if (token) {
                        this._token = token.replace('Bearer ', '');
                        this._isAuthenticated = true;
                    }

                    return !!token;
                }),
                catchError(() => of(false))
            );
    }

    logout() {
        this.http
            .post<void>(`${environment.apiUrl}/logout`, null)
            .subscribe(() => {
                this._isAuthenticated = false;
                this._token = '';
                this.router.navigate(['login']);
            });
    }
}
