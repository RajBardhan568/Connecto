import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PostsService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // --- US 03: Create Post ---
  createPost(postData: { content: string, imageUrl?: string }): Observable<any> {
    // Calls: POST /api/posts
    return this.http.post(`${this.apiUrl}/posts`, postData);
  }

  // --- US 09: Get News Feed (Posts from friends + self) ---
  getNewsFeed(): Observable<any> {
    // Calls: GET /api/posts/feed
    return this.http.get(`${this.apiUrl}/posts/feed`);
  }

  // --- US 09: Like Post ---
  likePost(postId: number): Observable<any> {
    // Calls: PUT /api/posts/{postId}/like
    return this.http.put(`${this.apiUrl}/posts/${postId}/like`, {});
  }
}