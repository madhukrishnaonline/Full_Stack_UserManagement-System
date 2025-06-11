import { Component } from '@angular/core';
import { UserAuthService } from '../../services/user-auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header-page',
  templateUrl: './header-page.component.html',
  styleUrls: ['./header-page.component.css']
})
export class HeaderPageComponent {
  constructor(private authService: UserAuthService, private route: Router) { }

  firstName: string | null = '';
  userId: string | null = '';

  getFirstName() {
    return this.firstName = this.authService.getFirstName();
  }

  getUserId() {
    return this.userId = this.authService.getUserId();
  }

  isUserLoggedIn() {
    return this.authService.isUserLoggedIn();
  }

  logOut() {
    this.authService.logOut();
    this.route.navigate(['/login']);
  }
}
