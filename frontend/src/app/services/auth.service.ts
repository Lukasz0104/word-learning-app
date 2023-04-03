import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import jwt_decode, { JwtPayload } from 'jwt-decode';
import { Observable, catchError, map, of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoginCredentials } from '../models/login-credentials';
import { RegistrationDto } from '../models/registration-dto';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private _isAuthenticated = false;
    private _token = '';
    private _username = '';

    get isAuthenticated() {
        return this._isAuthenticated;
    }

    get token() {
        return this._token;
    }

    get username() {
        return this._username;
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
                        this._username =
                            jwt_decode<JwtPayload>(token).sub ?? '';
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
