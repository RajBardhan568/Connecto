// src/app/components/login/login.component.ts

import { Component , OnInit} from '@angular/core';
// Import CommonModule if needed for structural directives, but often implicit in newer versions.
import { Router } from '@angular/router'; 
import { AuthService, LoginDTO } from '../../services/auth'; // NEW IMPORT
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms'; // NEW IMPORTS
@Component({
  selector: 'app-login',
  standalone: true,
  // You need to import NgIf, NgFor, RouterLink/RouterLinkActive etc.
  imports: [], 
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent implements OnInit {
    loginForm!: FormGroup;
    isLoading = false;

    constructor(private router: Router, private authService: AuthService, private fb: FormBuilder) { }
    
    ngOnInit(): void {
        // Simple form setup for login
        this.loginForm = this.fb.group({
            emailOrPhone: ['', Validators.required],
            password: ['', Validators.required]
        });
    }

    onLogin() {
        if (this.loginForm.invalid) {
            this.loginForm.markAllAsTouched();
            return;
        }
        
        this.isLoading = true;
        const loginData: LoginDTO = this.loginForm.value;

        this.authService.login(loginData).subscribe({
            next: (response) => {
                this.isLoading = false;
                console.log('Login successful:', response);
                // Navigate to the main application shell
                this.router.navigate(['/main/home']); 
            },
            error: (err) => {
                this.isLoading = false;
                alert('Login Failed: ' + err.message); // Display error
            }
        });
    }

    navigateToRegistration() {
        this.router.navigate(['/register']); 
    }
}