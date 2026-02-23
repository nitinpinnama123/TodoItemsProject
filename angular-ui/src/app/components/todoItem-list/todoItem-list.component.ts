import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TodoItem, TodoItemStatus } from '../../models/todoItem.model';
import { User } from '../../models/user.model';
import { TodoItemService } from '../../services/todoItem.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-todoItem-list',
  templateUrl: './todoItem-list.component.html',
  styleUrls: ['./todoItem-list.component.css']
})
export class TodoItemListComponent implements OnInit {
  todoItems: TodoItem[] = [];
  users: User[] = [];
  loading = false;
  error = '';
  
  // Filters
  selectedUserId?: number;
  selectedStatus?: TodoItemStatus;
  
  // Make TodoItemStatus enum available in template
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
        this.error = 'Failed to load todoItems';
        this.loading = false;
        console.error('Error loading todoItems:', err);
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
    this.router.navigate(['/items', id]);
  }

  editTodoItem(id: number): void {
    this.router.navigate(['/items', id, 'edit']);
  }

  deleteTodoItem(id: number): void {
    if (confirm('Are you sure you want to delete this todoItem?')) {
      this.todoItemService.deleteTodoItem(id).subscribe({
        next: () => {
          this.loadTodoItems();
        },
        error: (err) => {
          this.error = 'Failed to delete todoItem';
          console.error('Error deleting todoItem:', err);
        }
      });
    }
  }

  createTodoItem(): void {
    this.router.navigate(['/items/new']);
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
        this.error = 'Failed to update todoItem status';
        console.error('Error updating status:', err);
      }
    });
  }
}
