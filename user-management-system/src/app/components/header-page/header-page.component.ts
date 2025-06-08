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

  firstName: string | null = null;

  logOut() {
    if (this.authService.isUserLoggedIn()) {
      this.authService.logOut();
    } else {
      this.route.navigate(['/login']);
    }
  }
}
