import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule, NgFor } from '@angular/common';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-friends',
  standalone: true,
  imports: [CommonModule, NgFor],
  templateUrl: './friends.html',
  styleUrls: ['./friends.css'], // ✅ corrected from 'styleUrl'
})
export class FriendsComponent {
  pendingRequests: any[] = [];
  allFriends: any[] = [];

  confirmRequest(request: any) {
    // handle the specific request
  }

  deleteRequest(request: any) {
    // handle the specific request 
  }

  toggleFriendRequest(request: any, isConfirmed: boolean) {
    // use both arguments
  }

  viewProfile(request: any) {
    // logic to view profile, e.g., navigate to profile page
    console.log('Viewing profile for:', request);
  }

  unfriend(request: any) {
    // logic to remove or unfriend the user
    console.log('Unfriending:', request);
  }

  suggestedUsers: any[] = [];

}
