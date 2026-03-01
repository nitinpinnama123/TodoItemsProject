import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TodoItem, TodoItemStatus, Subtask, SubtaskRequest } from '../../models/todoItem.model';
import { User } from '../../models/user.model';
import { TodoItemService } from '../../services/todoItem.service';
import { UserService } from '../../services/user.service';
import { SubtaskService } from '../../services/subtask.service';

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

  // Track new subtask input for each task
  newSubtaskTitles: { [todoItemId: number]: string } = {};
  // Track expanded tasks for collapsible subtasks
  expandedTodoItems: Set<number> = new Set();

  constructor(
    private todoItemService: TodoItemService,
    private subtaskService: SubtaskService,
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

  toggleTodoItemStatus(todoItemId: number): void {
    this.todoItemService.toggleTodoItemComplete(todoItemId).subscribe({
      next: () => {
        this.loadTodoItems();
      },
      error: (err: any) => {
        this.error = 'Failed to update todoItem status';
        console.error('Error updating status:', err);
      }
    });
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
      next: (updatedTodo) => {
        this.loadTodoItems();

      },
      error: (err) => {
        this.error = 'Failed to update todoItem status';
        console.error('Error updating status:', err);
      }
    });
  }

  // ==================== SUBTASK METHODS ====================
  toggleTodoItemExpanded(todoItemId: number): void {
      if (this.expandedTodoItems.has(todoItemId)) {
        this.expandedTodoItems.delete(todoItemId);
      } else {
        this.expandedTodoItems.add(todoItemId);
      }
    }

    isTodoItemExpanded(todoItemId: number): boolean {
      return this.expandedTodoItems.has(todoItemId);
    }

    addSubtask(todoItemId: number): void {
      const title = this.newSubtaskTitles[todoItemId];

      if (!title || !title.trim()) {
        return;
      }

      const request: SubtaskRequest = {
        title: title,
        completed: false
      };

      this.subtaskService.createSubtask(todoItemId, request).subscribe({
        next: () => {
          this.newSubtaskTitles[todoItemId] = '';
          this.loadTodoItems(); // Reload to get updated subtasks
        },
        error: (err) => {
          this.error = 'Failed to create subtask';
          console.error('Error creating subtask:', err);
        }
      });
    }

    toggleSubtask(todoItemId: number, subtaskId: number): void {
      this.subtaskService.toggleSubtaskComplete(todoItemId, subtaskId).subscribe({
        next: () => {
          this.loadTodoItems(); // Reload to get updated state
        },
        error: (err) => {
          this.error = 'Failed to update subtask';
          console.error('Error updating subtask:', err);
        }
      });
    }

    deleteSubtask(todoItemId: number, subtaskId: number): void {
      if (!confirm('Are you sure you want to delete this subtask?')) {
        return;
      }

      this.subtaskService.deleteSubtask(todoItemId, subtaskId).subscribe({
        next: () => {
          this.loadTodoItems(); // Reload to get updated list
        },
        error: (err) => {
          this.error = 'Failed to delete subtask';
          console.error('Error deleting subtask:', err);
        }
      });
    }

    // Get subtask completion statistics for a task
    getSubtaskProgress(todoItem: TodoItem): { completed: number; total: number; percentage: number } {
      if (!todoItem.subtasks || todoItem.subtasks.length === 0) {
        return { completed: 0, total: 0, percentage: 0 };
      }

      const total = todoItem.subtasks.length;
      const completed = todoItem.subtasks.filter(s => s.completed).length;
      const percentage = Math.round((completed / total) * 100);

      return { completed, total, percentage };
    }

    // Check if all subtasks are complete
    areAllSubtasksComplete(todoItem: TodoItem): boolean {
      if (!todoItem.subtasks || todoItem.subtasks.length === 0) {
        return false;
      }
      return todoItem.subtasks.every(s => s.completed);
    }



}
