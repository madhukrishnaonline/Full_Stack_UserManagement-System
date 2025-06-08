import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserModelContract } from '../../contracts/user-model.contract';
import { UserAuthService } from '../../services/user-auth.service';
import { UserMgmtServiceService } from '../../services/user-mgmt-service.service';

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.css']
})
export class DashboardPageComponent implements OnInit {
  userObj: UserModelContract = {
    id: '',
    username: '',
    password: '',
    fullName: '',
    email: '',
    mobile: 0,
    address: ''
  }

  constructor(private route: Router, private activatedRoute: ActivatedRoute, private userService: UserMgmtServiceService, private authService: UserAuthService) { }

  firstName: string | null = null;
  userId: string | null = null;

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(parameters => {
      this.firstName = parameters['name'];
    });
    this.activatedRoute.queryParams.subscribe(params => {
      this.userId = params['id'];
    });
    //fetch User Data By User Id
    this.fetchUserData(this.userId);
  }

  fetchUserData(id: string | null): void {
    this.userService.getUserById(id).subscribe({
      next: (response) => {
        this.userObj = response;
      },
      error: (error) => console.error(error)
    });
  }

  fetchingDetails: boolean = false;
  isEditable: boolean = false;
  isUpdating: boolean = false;
  response: string = '';

  toggleEdit() {
    this.isEditable = !this.isEditable;
  }
  logOut() {
    this.authService.logOut();
    this.route.navigateByUrl('/login');
  }

  updateData() {

  }
  changeUserName() {

  }
}