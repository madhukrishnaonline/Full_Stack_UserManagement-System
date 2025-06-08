import { UserAuthService } from './user-auth.service';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root' })
export class InterceptorService implements HttpInterceptor {

    constructor(private authService: UserAuthService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = this.authService.isTokenAvailable();
        console.log("Expiration Time :: "+this.authService.extractExpirationTime());
        if (token) {
            const clonedReq = req.clone({
                setHeaders: {
                    Authorization: token
                }
            });
            return next.handle(clonedReq);
        }
        return next.handle(req);
    }
}