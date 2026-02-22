import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { TodoItem, TodoItemStatus, TodoItemRequest} from '../models/todoItem.model';

@Injectable({
    providedIn: 'root'
    })
export class TodoItemService {
    private apiUrl = `${environment.apiUrl}/TodoItems`;
    constructor(private http: HttpClient) {}

    getAllTodoItems(userId?: number, status?: TodoItemStatus): Observable<TodoItem[]> {
        let params = new HttpParams();

        if (userId !== undefined) {
          params = params.set('userId', userId.toString());
        }

        if (status !== undefined) {
          params = params.set('status', status);
        }

        return this.http.get<TodoItem[]>(this.apiUrl, { params });
    }

    getTodoItemById(id: number): Observable<TodoItem> {
        return this.http.get<TodoItem>(`${this.apiUrl}/${id}`);
    }

    getTodoItemsByUserId(userId: number): Observable<TodoItem[]> {
        return this.getAllTodoItems(userId);
      }

      getTodoItemsByStatus(status: TodoItemStatus): Observable<TodoItem[]> {
        return this.getAllTodoItems(undefined, status);
      }

      createTodoItem(TodoItemRequest: TodoItemRequest): Observable<TodoItem> {
        return this.http.post<TodoItem>(this.apiUrl, todoItemRequest);
      }

      updateTodoItem(id: number, todoItem: TodoItem): Observable<TodoItem> {
        return this.http.put<TodoItem>(`${this.apiUrl}/${id}`, todoItem);
      }

      assignTodoItemToUser(todoItemId: number, userId: number): Observable<TodoItem> {
        return this.http.patch<TodoItem>(`${this.apiUrl}/${todoItemId}/assign/${userId}`, {});
      }

      unassignTodoItem(todoItemId: number): Observable<TodoItem> {
        return this.http.patch<TodoItem>(`${this.apiUrl}/${todoItemId}/unassign`, {});
      }

      updateTodoItemStatus(todoItemId: number, status: TodoItemStatus): Observable<TodoItem> {
        const params = new HttpParams().set('status', status);
        return this.http.patch<TodoItem>(`${this.apiUrl}/${todoItemId}/status`, {}, { params });
      }

      deleteTodoItem(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
      }

}