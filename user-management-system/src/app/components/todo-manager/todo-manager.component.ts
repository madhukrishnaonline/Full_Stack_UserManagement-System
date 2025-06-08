import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TodoMgmtService } from '../../services/user-todo-mgmt.service';
import { TodoModelContract } from '../../contracts/todo-model.contract';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-todo-manager',
  templateUrl: './todo-manager.component.html',
  styleUrls: ['./todo-manager.component.css']
})
export class TodoManagerComponent implements OnInit {
  todoObject: TodoModelContract = {
    name: '',
    description: '',
    startTime: '',
    endTime: '',
    completed: null,
    userId: ''
  };

  constructor(private activatedRoute: ActivatedRoute, private todoService: TodoMgmtService, private router: Router) { }
  userId: string = '';
  todoId: any;
  firstName: string = '';

  getTodoData() {
    this.todoService.fetchTodoById(this.todoId).subscribe({
      next: (todoResponse) => {
        // console.log("todoResponse " + JSON.stringify(todoResponse));
        this.todoObject = todoResponse;
      },
      error: (errorResponse) => console.error(errorResponse)
    })
  }
  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.userId = params['id'];
      this.todoId = params['todoId'];
    })
    this.activatedRoute.params.subscribe(params => {
      this.firstName = params['name'];
    })
    if (this.todoId) {
      this.saveOrUpdate = 'Update';
      this.disableStartTime = true;
      this.getTodoData();
    }
  }
  saveOrUpdate: string = 'Create';

  // Create Todo
  createTodo(): void {
    this.todoObject.userId = this.userId;
    this.todoService.createTodo(this.todoObject).subscribe({
      next: (response) => {
        this.router.navigate(['user/dashboard/todo_list', this.firstName], { queryParams: { id: this.userId } });
        console.log(response);
      },
      error: (errorresponse) => {
        console.error(errorresponse);
      }
    })
  }

  // Update Todo
  disableStartTime: boolean = false;
  updateTodo() {
    this.todoObject.userId = this.userId;
    console.log("Todo Data :: " + JSON.stringify(this.todoObject));
    this.todoService.updateTodo(this.todoObject).subscribe({
      next: (response) => {
        // console.log(response);
        this.router.navigate(['user/dashboard/todo_list', this.firstName], { queryParams: { id: this.userId } });
      },
      error: (error) => {
        console.error(error)
      }
    });
  }

  submit(todoData: NgForm) {
    if (this.todoId) {
      this.updateTodo();
    } else {
      this.createTodo();
    }
  }
}
