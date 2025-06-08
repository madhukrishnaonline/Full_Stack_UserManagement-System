import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { TodoModelContract } from "../contracts/todo-model.contract";

declare const baseUserTodoUrl: string;
@Injectable({
    providedIn: 'root'
})
export class TodoMgmtService {
    constructor(private http: HttpClient) { }

    fetchAllTodos(): Observable<TodoModelContract[]> {
        return this.http.get<TodoModelContract[]>(`${baseUserTodoUrl}getAllTodos`);
    }

    fetchAllTodosByUserId(id: Number): Observable<TodoModelContract[]> {
        return this.http.get<TodoModelContract[]>(`${baseUserTodoUrl}getAllTodos/${id}`);
    }

    fetchTodoById(id: number): Observable<TodoModelContract> {
        return this.http.get<TodoModelContract>(`${baseUserTodoUrl}getTodo/${id}`);
    }

    createTodo(todoRequest: TodoModelContract): Observable<TodoModelContract> {
        return this.http.post<TodoModelContract>(`${baseUserTodoUrl}create`, todoRequest);
    }

    updateTodo(todoRequest: TodoModelContract): Observable<TodoModelContract> {
        return this.http.put<TodoModelContract>(`${baseUserTodoUrl}update`, todoRequest);
    }

    deleteTodoById(id: number | undefined): Observable<string> {
        return this.http.delete(`${baseUserTodoUrl}delete/${id}`, { responseType: 'text' });
    }

    deleteAllUserTodos(userId: string): Observable<string> {
        return this.http.delete(`${baseUserTodoUrl}delete/todos/${userId}`, { responseType: 'text' });
    }
}