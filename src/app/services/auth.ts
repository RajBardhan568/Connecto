// src/app/services/auth.service.ts

import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

// Define Interface for DTO (recommended for type safety)
export interface RegistrationDTO {
    firstName: string;
    surname: string;
    emailAddress: string;
    dateOfBirth: string;
    gender: string;
    password: string;
    confirmPassword: string;
}

// Define Interface for Response (assuming the backend returns this structure)
export interface UserResponse {
    id: number;
    firstName: string;
    emailAddress: string;
}

// Define interface for login payload
export interface LoginDTO {
    emailOrPhone: string;
    password: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    
    // Assuming the API Gateway runs on port 8080 (or your configured port)
    private apiUrl = 'http://localhost:8080/api/users'; 

    constructor(private http: HttpClient) { }

    /**
     * Calls the User Microservice registration endpoint.
     * @param data The UserRegistrationDTO payload.
     * @returns An Observable of the registration response.
     */
    register(data: RegistrationDTO): Observable<UserResponse> {
        const registrationUrl = `${this.apiUrl}/register`;
        
        // Use the POST method with the registration data
        return this.http.post<UserResponse>(registrationUrl, data)
            .pipe(
                catchError(this.handleError) // Centralized error handling
            );
    }
    
    // --- Error Handling Function ---
    private handleError(error: HttpErrorResponse) {
        let errorMessage = 'An unknown error occurred!';
        if (error.error instanceof ErrorEvent) {
            // Client-side network error
            errorMessage = `Network Error: ${error.error.message}`;
        } else {
            // Backend returned an unsuccessful response code (e.g., 400, 409)
            // The response body (error.error) contains the centralized exception message.
            errorMessage = error.error || `Server Error: ${error.status} ${error.statusText}`;
        }
        console.error(errorMessage);
        // Return an observable with a user-facing error message
        return throwError(() => new Error(errorMessage));
    }



    login(data: LoginDTO): Observable<UserResponse> {
        const loginUrl = `${this.apiUrl}/login`;
        
        return this.http.post<UserResponse>(loginUrl, data)
            .pipe(
                // In a real app, successful login would store a JWT/Token here
                catchError(this.handleError)
            );
    }
    
    logout(): void {
        // In a real app: Clear the JWT/Token from local storage
        localStorage.removeItem('authToken');
        // Clear user session data
    }
}