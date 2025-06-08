import { inject, Injectable } from "@angular/core";
import { CanActivate, CanActivateFn, Router } from "@angular/router";
import { UserAuthService } from "../services/user-auth.service";

export const SecurityConfigGuard: CanActivateFn = (route, state) => {
  const authService = inject(UserAuthService);
  const router = inject(Router);

  if (!authService.isUserLoggedIn()) {
    authService.logOut();
    router.navigate(['/login']);
    return false;
  } else {
    return true;
  }
}