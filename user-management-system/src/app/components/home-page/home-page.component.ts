import { Component, OnInit } from '@angular/core';
import { UserMgmtServiceService } from '../../services/user-mgmt-service.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {
  constructor(private userService: UserMgmtServiceService) { }
  homeMessage: string = '';

  ngOnInit(): void {
    this.userService.getHomePage().subscribe({
      next: (response) => {
        this.homeMessage = response;
      },
      error: (error) => console.error(error)
    });
  }
  
}
