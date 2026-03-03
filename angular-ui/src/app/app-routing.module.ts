import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserListComponent } from './components/user-list/user-list.component';
import { UserFormComponent } from './components/user-form/user-form.component';
import { TodoItemListComponent } from './components/todoItem-list/todoItem-list.component';
import { TodoItemFormComponent } from './components/todoItem-form/todoItem-form.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  // ===== PUBLIC ROUTES - NO GUARD =====
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  // Protected paths
  { path: 'users', component: UserListComponent },
  { path: 'users/new', component: UserFormComponent },
  { path: 'users/:id', component: UserFormComponent },
  { path: 'users/:id/edit', component: UserFormComponent },

  { path: 'items', component: TodoItemListComponent, canActivate: [AuthGuard] },
  { path: 'items/new', component: TodoItemFormComponent, canActivate: [AuthGuard] },
  { path: 'items/:id', component: TodoItemFormComponent, canActivate: [AuthGuard] },
  { path: 'items/:id/edit', component: TodoItemFormComponent, canActivate: [AuthGuard] },
  // Default paths
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/items' }


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
