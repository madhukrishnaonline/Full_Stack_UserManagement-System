import { Component } from '@angular/core';
import { UserMgmtServiceService } from '../../services/user-mgmt-service.service';
import { Router } from '@angular/router';
import { AuthServiceComponent } from 'projects/spring-boot-crudproject/src/app/components/services/auth.service';
import { UserAuthService } from '../../services/user-auth.service';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent {
  constructor(private userService: UserMgmtServiceService, private router: Router, private authService: UserAuthService) { }

  username: string = "";
  password: string = "";

  isLoginSuccess: boolean = false;
  loginUser() {
    this.isLoginSuccess = true;
    //login object
    const data = {
      username: this.username,
      password: this.password
    }
    // console.log(data);
    //login request
    this.userService.loginUser(data).subscribe({
      next: (response) => {
        this.isLoginSuccess = false;
        //redirect to dashboard
        const firstName = response.headers.get('FirstName');
        const userId = response.headers.get("UserId");
        this.router.navigate(['/user/dashboard', firstName],{queryParams:{id:userId}});
      },
      error: (error) => {
        console.log(error)
        this.isLoginSuccess = false;
      }
    });
  }
}
