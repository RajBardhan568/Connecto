import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth'; // NEW IMPORT
import { Router } from '@angular/router';
@Component({
  selector: 'app-main-shell',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './main-shell.html',
  styleUrls: ['./main-shell.css'],
})
export class MainShellComponent {



  constructor(private router: Router, private authService: AuthService) { } 

    onLogout() {
        // Call service method to clear token/session
        this.authService.logout(); 
        
        // Navigate back to the login screen
        this.router.navigate(['/login']);
    }
}