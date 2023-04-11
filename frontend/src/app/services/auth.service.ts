import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import jwt_decode, { JwtPayload } from 'jwt-decode';
import { Observable, catchError, map, of, tap } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoginCredentials } from '../models/login-credentials';
import { RegistrationDto } from '../models/registration-dto';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly TOKEN_KEY = '__token';
    private readonly USERNAME_KEY = '__username';

    private tokenRefreshInterval = 30 * 60 * 1000; // 30 mins

    private _isAuthenticated = false;
    private _token = '';
    private _username = '';

    private intervalId = -1;

    get isAuthenticated() {
        return this._isAuthenticated;
    }

    get token() {
        return this._token;
    }

    get username() {
        return this._username;
    }

    constructor(private http: HttpClient, private router: Router) {
        this._token = localStorage.getItem(this.TOKEN_KEY) || '';
        this._username = localStorage.getItem(this.USERNAME_KEY) || '';

        this._isAuthenticated = !!this._token && !!this._username;

        if (this._isAuthenticated) {
            this.intervalId = window.setInterval(() => {
                this.refreshToken();
            }, this.tokenRefreshInterval);
        }
    }

    login(credentials: LoginCredentials) {
        return this.http
            .post<void>(`${environment.apiUrl}/login`, credentials, {
                observe: 'response'
            })
            .pipe(
                map((res) => {
                    const token = res.headers.get('Authorization');
                    if (token) {
                        this.handleToken(token);
                        this.intervalId = window.setInterval(() => {
                            this.refreshToken();
                        }, this.tokenRefreshInterval);
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
                this._username = '';

                this.clearLocalStorage();

                window.clearInterval(this.intervalId);

                this.router.navigate(['login']);
            });
    }

    register(dto: RegistrationDto): Observable<string> {
        return this.http
            .post(`${environment.apiUrl}/register`, dto, {
                responseType: 'text'
            })
            .pipe(
                catchError((err: HttpErrorResponse) => {
                    return of(err.error as string);
                })
            );
    }

    private refreshToken() {
        this.http
            .post(`${environment.apiUrl}/refresh-token`, null, {
                observe: 'response'
            })
            .pipe(
                tap((res) => {
                    const token = res.headers.get('Authorization');
                    if (token) {
                        this.handleToken(token);
                    }
                })
            )
            .subscribe();
    }

    private handleToken(token: string): void {
        this._username = jwt_decode<JwtPayload>(token).sub ?? '';
        this._token = token.replace('Bearer ', '');
        this._isAuthenticated = true;

        localStorage.setItem(this.TOKEN_KEY, this._token);
        localStorage.setItem(this.USERNAME_KEY, this.username);
    }

    private clearLocalStorage() {
        localStorage.removeItem(this.TOKEN_KEY);
        localStorage.removeItem(this.USERNAME_KEY);
    }

    checkUsername(username: string): Observable<boolean> {
        return this.http
            .head(`${environment.apiUrl}/users`, {
                params: { username }
            })
            .pipe(
                map(() => false),
                catchError(() => of(true))
            );
    }

    checkEmailAddress(email: string): Observable<boolean> {
        return this.http
            .head(`${environment.apiUrl}/users`, {
                params: { email }
            })
            .pipe(
                map(() => false),
                catchError(() => of(true))
            );
    }
}
