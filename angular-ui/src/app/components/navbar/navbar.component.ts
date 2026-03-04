import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
//import { UserService } from '../../services/user.service';
import { AuthResponse } from '../../models/auth.model';
import { AuthService } from '../../services/auth.service';



@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrl: './navbar.component.css'
    })
export class AppNavbarComponent {
    title = 'Todo Item Management System';
    currentUser$: Observable<AuthResponse | null>;
    loggingOut = false;

    constructor(
        private authService: AuthService,
        private router: Router
    ) {
        //this.currentUser$ = this.authService.currentUser$;
        this.currentUser$ = this.authService.currentUser$;
    }

    logout() {
        this.router.navigate(['/login']);

        console.log('Logout clicked');

        this.loggingOut = true;

        this.authService.logout().subscribe({
            next: () => {
                console.log('Logout successful');
                this.loggingOut = false;
            },
            error: (err: any) => {
                console.error('Logout error:', err);
                this.authService.logoutInstant();  // Fallback
                this.loggingOut = false;
            }
       });
    }
}