import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Task, TaskStatus } from '../../models/task.model';
import { User } from '../../models/user.model';
import { TaskService } from '../../services/task.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];
  users: User[] = [];
  loading = false;
  error = '';
  
  // Filters
  selectedUserId?: number;
  selectedStatus?: TaskStatus;
  
  // Make TaskStatus enum available in template
  taskStatuses = Object.values(TaskStatus);

  constructor(
    private taskService: TaskService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUsers();
    this.loadTasks();
  }

  loadUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.users = users;
      },
      error: (err) => {
        console.error('Error loading users:', err);
      }
    });
  }

  loadTasks(): void {
    this.loading = true;
    this.error = '';
    
    this.taskService.getAllTasks(this.selectedUserId, this.selectedStatus).subscribe({
      next: (tasks) => {
        this.tasks = tasks;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load tasks';
        this.loading = false;
        console.error('Error loading tasks:', err);
      }
    });
  }

  onFilterChange(): void {
    this.loadTasks();
  }

  clearFilters(): void {
    this.selectedUserId = undefined;
    this.selectedStatus = undefined;
    this.loadTasks();
  }

  viewTask(id: number): void {
    this.router.navigate(['/tasks', id]);
  }

  editTask(id: number): void {
    this.router.navigate(['/tasks', id, 'edit']);
  }

  deleteTask(id: number): void {
    if (confirm('Are you sure you want to delete this task?')) {
      this.taskService.deleteTask(id).subscribe({
        next: () => {
          this.loadTasks();
        },
        error: (err) => {
          this.error = 'Failed to delete task';
          console.error('Error deleting task:', err);
        }
      });
    }
  }

  createTask(): void {
    this.router.navigate(['/tasks/new']);
  }

  getStatusClass(status: TaskStatus): string {
    const statusClasses: { [key in TaskStatus]: string } = {
      [TaskStatus.PENDING]: 'status-pending',
      [TaskStatus.IN_PROGRESS]: 'status-in-progress',
      [TaskStatus.COMPLETED]: 'status-completed',
      [TaskStatus.CANCELLED]: 'status-cancelled'
    };
    return statusClasses[status];
  }

  updateStatus(taskId: number, newStatus: TaskStatus): void {
    this.taskService.updateTaskStatus(taskId, newStatus).subscribe({
      next: () => {
        this.loadTasks();
      },
      error: (err) => {
        this.error = 'Failed to update task status';
        console.error('Error updating status:', err);
      }
    });
  }
}
