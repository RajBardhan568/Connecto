import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { AuthService } from './auth'; // Use AuthService to get ID

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient, private authService: AuthService) { }

  // --- US 04: View Profile ---
  getProfile(userId: number): Observable<any> {
    // Calls: /api/users/{userId}/profile
    return this.http.get(`${this.apiUrl}/users/${userId}/profile`);
  }

  // --- US 04: Update Profile ---
  updateProfile(updateData: any): Observable<any> {
    const userId = this.authService.getUserId();
    if (!userId) {
        return new Observable(observer => observer.error('User not logged in.'));
    }
    // Calls: PUT /api/users/{userId}/profile
    return this.http.put(`${this.apiUrl}/users/${userId}/profile`, updateData);
  }
}