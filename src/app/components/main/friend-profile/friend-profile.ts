import { Component } from '@angular/core';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-friend-profile',
  imports: [NgIf],
  templateUrl: './friend-profile.html',
  styleUrl: './friend-profile.css',
})
export class FriendProfileComponent {

  friend: any = {
  name: 'Sample Friend',
  postsVisible: true
  // add other fields like id, email, etc.
};
unfriend(friendId: number): void {
  console.log('Unfriending friend with ID:', friendId);
  // Add logic to remove friend, call service, etc.
}

 
}
