import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FriendsService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // --- US 08: Send Friend Request ---
  sendRequest(receiverId: number): Observable<any> {
    // Calls: /api/friends/send/{receiverId}
    return this.http.post(`${this.apiUrl}/friends/send/${receiverId}`, {});
  }

  // --- US 05: View Pending Requests ---
  getPendingRequests(): Observable<any> {
    // Calls: /api/friends/requests
    return this.http.get(`${this.apiUrl}/friends/requests`);
  }
  
  // --- US 05: Accept Request (Using the request's ID) ---
  acceptRequest(requestId: number): Observable<any> {
    // Calls: PUT /api/friends/accept/{requestId}
    return this.http.put(`${this.apiUrl}/friends/accept/${requestId}`, {});
  }

  // --- US 08: Unfriend ---
  unfriend(friendId: number): Observable<any> {
    // Calls: DELETE /api/friends/unfriend/{friendId}
    return this.http.delete(`${this.apiUrl}/friends/unfriend/${friendId}`);
  }

  // NOTE: Finding all friends and finding new friends will require additional endpoints
  // for the Friend Request MS to expose, but the basic functionality is integrated here.
}