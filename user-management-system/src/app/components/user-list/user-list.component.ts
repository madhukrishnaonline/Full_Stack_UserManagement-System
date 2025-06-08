import { Component, OnInit } from '@angular/core';
import { UserMgmtServiceService } from '../../services/user-mgmt-service.service';
import { UserModelContract } from '../../contracts/user-model.contract';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  usersList: UserModelContract[] = [];

  getUsersList() {
    return this.usersList || [];
  }
  constructor(private userService: UserMgmtServiceService) { }

  ngOnInit(): void {
    this.getAllUsers();
  }

  getAllUsers() {
    this.userService.getAllUsers().subscribe({
      next: (response) => {
        this.usersList = response;
      },
      error: (error) => console.error(error)
    })
  }
}
