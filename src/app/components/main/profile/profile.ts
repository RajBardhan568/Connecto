import { Component } from '@angular/core';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.css']
})

export class ProfileComponent {

  user: any = {
  name: 'John Doe',
  bio: ''
  // add other fields as needed
};

saveProfileChanges(): void {
  // logic to save profile changes
  console.log('Profile changes saved.');
}

}
