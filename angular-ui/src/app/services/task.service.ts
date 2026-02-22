import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Task, TaskStatus, TaskRequest} from '../models/task.model';

@Injectable({
    providedIn: 'root'
    })
export class TaskService {
    private apiUrl = `${environment.apiUrl}/tasks`;
    constructor(private http: HttpClient) {}

    getAllTasks(userId?: number, status?: TaskStatus): Observable<Task[]> {
        let params = new HttpParams();

        if (userId !== undefined) {
          params = params.set('userId', userId.toString());
        }

        if (status !== undefined) {
          params = params.set('status', status);
        }

        return this.http.get<Task[]>(this.apiUrl, { params });
    }

    getTaskById(id: number): Observable<Task> {
        return this.http.get<Task>(`${this.apiUrl}/${id}`);
    }

    getTasksByUserId(userId: number): Observable<Task[]> {
        return this.getAllTasks(userId);
      }

      getTasksByStatus(status: TaskStatus): Observable<Task[]> {
        return this.getAllTasks(undefined, status);
      }

      createTask(taskRequest: TaskRequest): Observable<Task> {
        return this.http.post<Task>(this.apiUrl, taskRequest);
      }

      updateTask(id: number, task: Task): Observable<Task> {
        return this.http.put<Task>(`${this.apiUrl}/${id}`, task);
      }

      assignTaskToUser(taskId: number, userId: number): Observable<Task> {
        return this.http.patch<Task>(`${this.apiUrl}/${taskId}/assign/${userId}`, {});
      }

      unassignTask(taskId: number): Observable<Task> {
        return this.http.patch<Task>(`${this.apiUrl}/${taskId}/unassign`, {});
      }

      updateTaskStatus(taskId: number, status: TaskStatus): Observable<Task> {
        const params = new HttpParams().set('status', status);
        return this.http.patch<Task>(`${this.apiUrl}/${taskId}/status`, {}, { params });
      }

      deleteTask(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
      }



}