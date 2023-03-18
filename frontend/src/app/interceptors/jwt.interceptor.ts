import {
    HttpEvent,
    HttpHandler,
    HttpInterceptor,
    HttpRequest
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    private readonly AUTHORIZATION_HEADER = 'Authorization';

    constructor(private authService: AuthService) {}

    intercept(
        request: HttpRequest<unknown>,
        next: HttpHandler
    ): Observable<HttpEvent<unknown>> {
        const token = this.authService.token;

        const clonedReq = request.clone({
            headers: request.headers.set(
                this.AUTHORIZATION_HEADER,
                `Bearer ${token}`
            )
        });

        return next.handle(clonedReq);
    }
}
