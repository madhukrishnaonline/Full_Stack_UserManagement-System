import { CanActivateFn, Router } from "@angular/router";
import { UserAuthService } from "../services/user-auth.service";
import { inject } from "@angular/core";

export const NoAuthRouteGuardSecurityConfig:CanActivateFn=(route,state)=>{
const authService = inject(UserAuthService);
const router = inject(Router);
    if(authService.isUserLoggedIn()){
      return false;
    }
    return true;
}