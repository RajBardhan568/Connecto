import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
  imports: [CommonModule]
})
export class HomeComponent {
  postUpdate(): void {
    // TODO: replace with real post logic (e.g., read textarea value, call a service)
    console.log('Post button clicked');
  }

  toggleLike(post: any): void {
    if (post) {
        post.isLiked = !post.isLiked;
        post.likes = post.isLiked ? post.likes + 1 : post.likes - 1;
    }
}

newsFeedData: any[] = [];


}
