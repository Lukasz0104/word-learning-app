import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoginCredentials } from '../models/login-credentials';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private _isAuthenticated = false;
    private token = '';

    get isAuthenticated() {
        return this._isAuthenticated;
    }

    constructor(private http: HttpClient) {}

    login(credentials: LoginCredentials) {
        return this.http
            .post<void>(`${environment.apiUrl}/login`, credentials, {
                observe: 'response'
            })
            .pipe(
                map((res) => {
                    const token = res.headers.get('Authorization');

                    if (token) {
                        this.token = token.replace('Bearer ', '');
                        this._isAuthenticated = true;
                    }

                    return !!token;
                })
            );
    }

    logout() {
        this.http
            .post<void>(`${environment.apiUrl}/logout`, null, {
                headers: {
                    Authorization: `Bearer ${this.token}`
                }
            })
            .subscribe(() => {
                this._isAuthenticated = false;
                this.token = '';
            });
    }
}
