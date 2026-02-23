import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TodoItem, TodoItemRequest, TodoItemStatus } from '../../models/todoItem.model';
import { User } from '../../models/user.model';
import { TodoItemService } from '../../services/todoItem.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-todoItem-form',
  templateUrl: './todoItem-form.component.html',
  styleUrls: ['./todoItem-form.component.css']
})
export class TodoItemFormComponent implements OnInit {
  todoItemForm: FormGroup;
  users: User[] = [];
  isEditMode = false;
  todoItemId?: number;
  loading = false;
  error = '';
  submitted = false;

  todoItemStatuses = Object.values(TodoItemStatus);

  constructor(
    private fb: FormBuilder,
    private todoItemService: TodoItemService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.todoItemForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      description: [''],
      status: [TodoItemStatus.PENDING, Validators.required],
      assignedToId: [null]
    });
  }

  ngOnInit(): void {
    this.loadUsers();
    
    const id = this.route.snapshot.paramMap.get('id');
    if (id && id !== 'new') {
      this.isEditMode = true;
      this.todoItemId = +id;
      this.loadTodoItem(this.todoItemId);
    }
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

  loadTodoItem(id: number): void {
    this.loading = true;
    this.todoItemService.getTodoItemById(id).subscribe({
      next: (todoItem) => {
        this.todoItemForm.patchValue({
          title: todoItem.title,
          description: todoItem.description,
          status: todoItem.status,
          assignedToId: todoItem.assignedTo?.id
        });
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load todoItem';
        this.loading = false;
        console.error('Error loading todoItem:', err);
      }
    });
  }

  get f() {
    return this.todoItemForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';

    if (this.todoItemForm.invalid) {
      return;
    }

    this.loading = true;
    const formValue = this.todoItemForm.value;

    if (this.isEditMode) {
      // For update, send TodoItem object
      const todoItem: TodoItem = {
        id: this.todoItemId,
        title: formValue.title,
        description: formValue.description,
        createdBy: formValue.createdBy,
        status: formValue.status
      };

      this.todoItemService.updateTodoItem(this.todoItemId!, todoItem).subscribe({
        next: () => {
          // If assignment changed, update it separately
          this.router.navigate(['/todoItems']);
        },
        error: (err) => {
          this.error = err.error?.message || 'Failed to update todoItem';
          this.loading = false;
          console.error('Error updating todoItem:', err);
        }
      });
    } else {
      // For create, send TodoItemRequest
      const todoItemRequest: TodoItemRequest = {
        title: formValue.title,
        description: formValue.description,
        status: formValue.status,
        assignedToId: formValue.assignedToId
      };

      this.todoItemService.createTodoItem(todoItemRequest).subscribe({
        next: () => {
          this.router.navigate(['/todoItems']);
        },
        error: (err) => {
          this.error = err.error?.message || 'Failed to create todoItem';
          this.loading = false;
          console.error('Error creating todoItem:', err);
        }
      });
    }
  }

  onCancel(): void {
    this.router.navigate(['/todoItems']);
  }
}
