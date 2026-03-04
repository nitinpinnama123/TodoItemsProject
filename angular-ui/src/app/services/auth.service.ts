import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of, tap, catchError } from 'rxjs';
import { LoginRequest, RegisterRequest, AuthResponse } from '../models/auth.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUserSubject = new BehaviorSubject<AuthResponse | null>(this.getUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
      const user = JSON.parse(localStorage.getItem('currentUser') || 'null');
      if (user) this.currentUserSubject.next(user);
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request).pipe(
      tap(response => this.setUserSession(response))
    );
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request).pipe(
      tap(response => this.setUserSession(response))
    );
  }

  logout(): Observable<any> {
    console.log('Logging out...');
    return this.http.post(`${this.apiUrl}/logout`, {}).
    pipe(
        catchError(err => {
                  console.log('Backend logout failed, continuing');
                  return of(null);  // Continue even if backend fails
                }),
        tap(() => this.clearUserData())
    );
  }

  logoutInstant(): void {
      this.clearUserData();
  }

  private setUserSession(response: AuthResponse): void {
    const user: AuthResponse = {
      userId: response.userId,
      username: response.username,
      name: response.name,
      email: response.email,
      role: response.role,
      token: response.token
    };
    localStorage.setItem('currentUser', JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  private clearUserData(): void {

    // 1. Clear localStorage
    localStorage.removeItem('currentUser');

    // 2. Clear BehaviorSubject
    this.currentUserSubject.next(null);

    // 3. Redirect to login
    this.router.navigate(['/login']);

    console.log('User logged out, redirected');
  }

  private getUserFromStorage(): AuthResponse | null {
    const userJson = localStorage.getItem('currentUser');
    return userJson ? JSON.parse(userJson) : null;
  }

  getCurrentUser(): AuthResponse | null {
    return this.currentUserSubject.value;
  }

  isLoggedIn(): boolean {
    return this.currentUserSubject.value !== null;
  }

  getUserId(): number | null {
    return this.currentUserSubject.value?.userId || null;
  }
}
