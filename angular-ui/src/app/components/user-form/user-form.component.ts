import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../models/user.model';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnInit {
  userForm: FormGroup;
  isEditMode = false;
  userId?: number;
  loading = false;
  error = '';
  submitted = false;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.userForm = this.fb.group({
        fName: [''],
        lName: [''],
      username: ['', [Validators.required, Validators.minLength(2)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      email: ['', [Validators.required, Validators.email]],
      role: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id && id !== 'new') {
      this.isEditMode = true;
      this.userId = +id;
      this.loadUser(this.userId);
    }
  }

  loadUser(id: number): void {
    this.loading = true;
    this.userService.getUserById(id).subscribe({
      next: (user) => {
        this.userForm.patchValue({
          fName: user.fName,
          lName: user.lName,
          username: user.username,
          email: user.email,
          role: user.role
        });
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load user';
        this.loading = false;
        console.error('Error loading user:', err);
      }
    });
  }

  get f() {
    return this.userForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';

    if (this.userForm.invalid) {
      return;
    }

    this.loading = true;
    const user: User = this.userForm.value;

    const operation = this.isEditMode
      ? this.userService.updateUser(this.userId!, user)
      : this.userService.createUser(user);

    operation.subscribe({
      next: () => {
        this.router.navigate(['/users']);
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to save user';
        this.loading = false;
        console.error('Error saving user:', err);
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/users']);
  }
}
