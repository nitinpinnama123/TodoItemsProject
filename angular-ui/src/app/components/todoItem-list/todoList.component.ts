import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TodoItem, TodoItemStatus } from '../../models/todoItem.model';
import { User } from '../../models/user.model';
import { TodoItemService } from '../../services/todoItem.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TodoItemListComponent implements OnInit {
  todoItems: TodoItem[] = [];
  users: User[] = [];
  loading = false;
  error = '';
  
  // Filters
  selectedUserId?: number;
  selectedStatus?: TodoItemStatus;
  
  // Make TaskStatus enum available in template
  todoItemStatuses = Object.values(TodoItemStatus);

  constructor(
    private todoItemService: TodoItemService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUsers();
    this.loadTodoItems();
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

  loadTodoItems(): void {
    this.loading = true;
    this.error = '';
    
    this.todoItemService.getAllTodoItems(this.selectedUserId, this.selectedStatus).subscribe({
      next: (todoItems) => {
        this.todoItems = todoItems;
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
    this.loadTodoItems();
  }

  clearFilters(): void {
    this.selectedUserId = undefined;
    this.selectedStatus = undefined;
    this.loadTodoItems();
  }

  viewTodoItem(id: number): void {
    this.router.navigate(['/tasks', id]);
  }

  editTodoItem(id: number): void {
    this.router.navigate(['/tasks', id, 'edit']);
  }

  deleteTodoItem(id: number): void {
    if (confirm('Are you sure you want to delete this task?')) {
      this.todoItemService.deleteTodoItem(id).subscribe({
        next: () => {
          this.loadTodoItems();
        },
        error: (err) => {
          this.error = 'Failed to delete task';
          console.error('Error deleting task:', err);
        }
      });
    }
  }

  createTodoItem(): void {
    this.router.navigate(['/tasks/new']);
  }

  getStatusClass(status: TodoItemStatus): string {
    const statusClasses: { [key in TodoItemStatus]: string } = {
      [TodoItemStatus.PENDING]: 'status-pending',
      [TodoItemStatus.IN_PROGRESS]: 'status-in-progress',
      [TodoItemStatus.COMPLETED]: 'status-completed',
      [TodoItemStatus.CANCELLED]: 'status-cancelled'
    };
    return statusClasses[status];
  }

  updateStatus(todoItemId: number, newStatus: TodoItemStatus): void {
    this.todoItemService.updateTodoItemStatus(todoItemId, newStatus).subscribe({
      next: () => {
        this.loadTodoItems();
      },
      error: (err) => {
        this.error = 'Failed to update task status';
        console.error('Error updating status:', err);
      }
    });
  }
}
