import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserListComponent } from './components/user-list/user-list.component';
import { UserFormComponent } from './components/user-form/user-form.component';
import { TodoItemListComponent } from './components/todoItem-list/todoItem-list.component';
import { TodoItemFormComponent } from './components/todoItem-form/todoItem-form.component';

const routes: Routes = [
  { path: '', redirectTo: '/tasks', pathMatch: 'full' },
  { path: 'users', component: UserListComponent },
  { path: 'users/new', component: UserFormComponent },
  { path: 'users/:id', component: UserFormComponent },
  { path: 'users/:id/edit', component: UserFormComponent },

  { path: 'items', component: TodoItemListComponent },
  { path: 'items/new', component: TodoItemFormComponent },
  { path: 'items/:id', component: TodoItemFormComponent },
  { path: 'items/:id/edit', component: TodoItemFormComponent },
  { path: '**', redirectTo: '/items' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
