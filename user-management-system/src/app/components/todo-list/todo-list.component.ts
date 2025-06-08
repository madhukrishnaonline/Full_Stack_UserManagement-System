import { Component, OnInit } from '@angular/core';
import { TodoMgmtService } from '../../services/user-todo-mgmt.service';
import { ActivatedRoute } from '@angular/router';
import { TodoModelContract } from '../../contracts/todo-model.contract';

@Component({
  selector: 'app-todo-list',
  templateUrl: './todo-list.component.html',
  styleUrls: ['./todo-list.component.css']
})
export class TodoListComponent implements OnInit {
  todoList: TodoModelContract[] = [];
  userId: any;
  firstName: string = '';

  constructor(private activatedRoute: ActivatedRoute, private todoService: TodoMgmtService) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      this.firstName = params['name'];
    });
    this.activatedRoute.queryParams.subscribe(params => {
      this.userId = params['id'];
    });
    this.getAllTodos();
  }

  getAllTodos() {
    this.todoService.fetchAllTodos().subscribe({
      next: (todoResponse) => {
        this.todoList = todoResponse;
      },
      error: (errorResponse) => console.error(errorResponse)
    });
  }

  getTodoList(): TodoModelContract[] {
    return this.todoList || [];
  }

  successMsg: string = '';
  deleteTodo(id: number | undefined, index: number) {
    this.todoService.deleteTodoById(id).subscribe({
      next: (response) => {
        this.successMsg = response;
        this.todoList.splice(index, 1);
        this.getTodoList();
        // this.ngOnInit();
      },
      error: (errorResponse) => {
        console.error(errorResponse);
      }
    })
  }

}
