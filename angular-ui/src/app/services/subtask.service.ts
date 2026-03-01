import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Subtask, SubtaskRequest } from '../models/todoItem.model';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class SubtaskService {
  private apiUrl = `${environment.apiUrl}/items`; // Changed to /items

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  /**
   * Get headers with user authentication
   */
  private getHeaders(): HttpHeaders {
    const userId = this.authService.getUserId();
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'X-User-Id': userId ? userId.toString() : ''
    });
  }

  /**
   * Get all subtasks for a specific task
   * GET /api/items/{taskId}/subtasks
   */
  getSubtasks(taskId: number): Observable<Subtask[]> {
    return this.http.get<Subtask[]>(
      `${this.apiUrl}/${taskId}/subtasks`,
      { headers: this.getHeaders() }
    );
  }

  /**
   * Get a specific subtask by ID
   * GET /api/items/{taskId}/subtasks/{subtaskId}
   */
  getSubtaskById(taskId: number, subtaskId: number): Observable<Subtask> {
    return this.http.get<Subtask>(
      `${this.apiUrl}/${taskId}/subtasks/${subtaskId}`,
      { headers: this.getHeaders() }
    );
  }

  /**
   * Create a new subtask
   * POST /api/items/{taskId}/subtasks
   */
  createSubtask(taskId: number, request: SubtaskRequest): Observable<Subtask> {
    return this.http.post<Subtask>(
      `${this.apiUrl}/${taskId}/subtasks`,
      request,
      { headers: this.getHeaders() }
    );
  }

  /**
   * Update an existing subtask
   * PUT /api/items/{taskId}/subtasks/{subtaskId}
   */
  updateSubtask(taskId: number, subtaskId: number, request: SubtaskRequest): Observable<Subtask> {
    return this.http.put<Subtask>(
      `${this.apiUrl}/${taskId}/subtasks/${subtaskId}`,
      request,
      { headers: this.getHeaders() }
    );
  }

  /**
   * Toggle subtask completion status
   * PATCH /api/items/{taskId}/subtasks/{subtaskId}/toggle
   */
  toggleSubtaskComplete(taskId: number, subtaskId: number): Observable<Subtask> {
    return this.http.patch<Subtask>(
      `${this.apiUrl}/${taskId}/subtasks/${subtaskId}/toggle`,
      {},
      { headers: this.getHeaders() }
    );
  }

  /**
   * Delete a subtask
   * DELETE /api/items/{taskId}/subtasks/{subtaskId}
   */
  deleteSubtask(taskId: number, subtaskId: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${taskId}/subtasks/${subtaskId}`,
      { headers: this.getHeaders() }
    );
  }

  /**
   * Bulk create subtasks
   * POST /api/items/{taskId}/subtasks/bulk
   * (Optional - if your backend supports it)
   */
  createSubtasksBulk(taskId: number, requests: SubtaskRequest[]): Observable<Subtask[]> {
    return this.http.post<Subtask[]>(
      `${this.apiUrl}/${taskId}/subtasks/bulk`,
      requests,
      { headers: this.getHeaders() }
    );
  }

  /**
   * Mark all subtasks as complete
   * PATCH /api/items/{taskId}/subtasks/complete-all
   * (Optional - if your backend supports it)
   */
  completeAllSubtasks(taskId: number): Observable<Subtask[]> {
    return this.http.patch<Subtask[]>(
      `${this.apiUrl}/${taskId}/subtasks/complete-all`,
      {},
      { headers: this.getHeaders() }
    );
  }

  /**
   * Reorder subtasks
   * PUT /api/items/{taskId}/subtasks/reorder
   * (Optional - if your backend supports it)
   */
  reorderSubtasks(taskId: number, subtaskIds: number[]): Observable<void> {
    return this.http.put<void>(
      `${this.apiUrl}/${taskId}/subtasks/reorder`,
      { subtaskIds },
      { headers: this.getHeaders() }
    );
  }
}
