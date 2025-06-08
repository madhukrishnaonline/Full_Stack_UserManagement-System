import { Injectable } from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class UserAuthService {

    isTokenAvailable(): string | null {
        return sessionStorage.getItem('Auth_Token');
    }

    isTokenExpired(): boolean {
        if (this.isTokenAvailable()) {
            const expiry = this.extractExpirationTime();
            return expiry ? new Date() > expiry : false;
        }
        return false;
    }

    isUserLoggedIn(): boolean {
        const token = this.isTokenAvailable();
        return token !== null && !this.isTokenExpired();
    }

    extractExpirationTime(): Date | null {
        const token = this.isTokenAvailable();
        if (!token) return null;
        try {
            const tokenPayLoad = token.split(".")[1];
            const payLoadObject = JSON.parse(atob(tokenPayLoad));
            if (!payLoadObject.exp) return null;
            return new Date(payLoadObject.exp * 1000);
        } catch (e) {
            console.error("Invalid Token Format " + e);
            return null;
        }
    }

    logOut(): void {
        sessionStorage.removeItem("Auth_Token");
    }
}