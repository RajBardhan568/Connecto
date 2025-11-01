import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../environments/environment';


export interface UserResponse {
  token: string;
  userId: number;
  firstName: string;
}

export interface RegistrationDTO {
  firstName: string;
  surname: string;
  emailAddress: string;
  dateOfBirth: string;
  gender: string;
  password: string;
  confirmPassword: string;
}

export interface LoginDTO {
  emailAddress: string;
  password: string;
}



@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // --- US 01: Registration ---
  register(registrationData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/users/register`, registrationData);
  }

  // --- US 02: Login and JWT Storage ---
  login(loginData: any): Observable<any> {
    // Expected response: { token: '...', firstName: '...', userId: 1 }
    return this.http.post<any>(`${this.apiUrl}/users/login`, loginData).pipe(
      tap(response => {
        // Store the JWT and user data upon successful login
        localStorage.setItem('auth_token', response.token);
        localStorage.setItem('user_id', response.userId);
        localStorage.setItem('user_firstName', response.firstName);
      })
    );
  }

  // Helper methods
  getToken(): string | null {
    return localStorage.getItem('auth_token');
  }

  getUserId(): number | null {
    const id = localStorage.getItem('user_id');
    return id ? +id : null;
  }

    logout(): void {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user_id');
    localStorage.removeItem('user_firstName');
  }

}