import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Task, TaskRequest, TaskStatus } from '../../models/task.model';
import { User } from '../../models/user.model';
import { TaskService } from '../../services/task.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-task-form',
  templateUrl: './task-form.component.html',
  styleUrls: ['./task-form.component.css']
})
export class TaskFormComponent implements OnInit {
  taskForm: FormGroup;
  users: User[] = [];
  isEditMode = false;
  taskId?: number;
  loading = false;
  error = '';
  submitted = false;

  taskStatuses = Object.values(TaskStatus);

  constructor(
    private fb: FormBuilder,
    private taskService: TaskService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.taskForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      description: [''],
      status: [TaskStatus.PENDING, Validators.required],
      assignedToId: [null]
    });
  }

  ngOnInit(): void {
    this.loadUsers();
    
    const id = this.route.snapshot.paramMap.get('id');
    if (id && id !== 'new') {
      this.isEditMode = true;
      this.taskId = +id;
      this.loadTask(this.taskId);
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

  loadTask(id: number): void {
    this.loading = true;
    this.taskService.getTaskById(id).subscribe({
      next: (task) => {
        this.taskForm.patchValue({
          title: task.title,
          description: task.description,
          status: task.status,
          assignedToId: task.assignedTo?.id
        });
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load task';
        this.loading = false;
        console.error('Error loading task:', err);
      }
    });
  }

  get f() {
    return this.taskForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';

    if (this.taskForm.invalid) {
      return;
    }

    this.loading = true;
    const formValue = this.taskForm.value;

    if (this.isEditMode) {
      // For update, send Task object
      const task: Task = {
        id: this.taskId,
        title: formValue.title,
        description: formValue.description,
        createdBy: formValue.createdBy,
        status: formValue.status
      };

      this.taskService.updateTask(this.taskId!, task).subscribe({
        next: () => {
          // If assignment changed, update it separately
          this.router.navigate(['/tasks']);
        },
        error: (err) => {
          this.error = err.error?.message || 'Failed to update task';
          this.loading = false;
          console.error('Error updating task:', err);
        }
      });
    } else {
      // For create, send TaskRequest
      const taskRequest: TaskRequest = {
        title: formValue.title,
        description: formValue.description,
        status: formValue.status,
        assignedToId: formValue.assignedToId
      };

      this.taskService.createTask(taskRequest).subscribe({
        next: () => {
          this.router.navigate(['/tasks']);
        },
        error: (err) => {
          this.error = err.error?.message || 'Failed to create task';
          this.loading = false;
          console.error('Error creating task:', err);
        }
      });
    }
  }

  onCancel(): void {
    this.router.navigate(['/tasks']);
  }
}
