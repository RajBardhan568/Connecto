import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors, ReactiveFormsModule } from '@angular/forms';
import moment from 'moment'; // Now assumed to be installed and correctly imported
import { AuthService, RegistrationDTO, UserResponse } from '../../services/auth'; // <-- NEW IMPORT
import { NgIf } from '@angular/common'; // Explicitly imported for *ngIf in HTML

@Component({
  selector: 'app-registration',
  standalone: true,
  // Ensure NgIf is imported if using Angular 17+ or necessary for standalone components
  imports: [CommonModule, ReactiveFormsModule, RouterLink, NgIf], 
  templateUrl: './registration.html', // Note: Corrected to .component.html path
  styleUrls: ['./registration.css'] // Note: Corrected to .component.css path
})
export class RegistrationComponent implements OnInit {
  
    registrationForm!: FormGroup;
    isLoading = false; // Flag for button loading state

    // Inject AuthService
    constructor(
        private fb: FormBuilder, 
        private router: Router,
        private authService: AuthService // <-- INJECT THE AUTH SERVICE
    ) { }

    ngOnInit(): void {
        this.registrationForm = this.fb.group({
            // US 01: Capitalized, Alphanumeric
            firstName: ['', [Validators.required, Validators.pattern(/^[A-Z][a-zA-Z0-9]*$/)]],
            surname: ['', [Validators.required, Validators.pattern(/^[A-Z][a-zA-Z0-9]*$/)]],
            
            // US 01: Valid email format
            emailAddress: ['', [Validators.required, Validators.email]], 
            
            // US 01: Password & Confirm Password with custom validators
            password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(16), this.passwordComplexityValidator]],
            confirmPassword: ['', Validators.required],
            
            // US 01: >= 13 years old
            dateOfBirth: ['', [Validators.required, this.minAgeValidator(13)]],
            
            // US 01: Male, Female, Others
            gender: ['', [Validators.required, this.genderValidator]],
            
        }, {
            // Validator applied to the whole form group for password matching
            validators: this.passwordMatchValidator
        });
    }
    
    // Convenience getter for easy access to form fields in the HTML template (f['name'])
    get f() { return this.registrationForm.controls; }

    // --- CUSTOM VALIDATORS (Using Moment.js as per your provided code) ---

    // US 01: Minimum Age check (13 years)
    private minAgeValidator(minAge: number) {
        return (control: AbstractControl): ValidationErrors | null => {
            if (!control.value) {
                return null; // Don't validate if empty, leave to Validators.required
            }
            // Uses Moment.js for robust date handling
            const dateOfBirth = moment(control.value);
            const today = moment();
            const age = today.diff(dateOfBirth, 'years');
            
            return age >= minAge ? null : { minAge: true };
        };
    }

    // US 01: Password Complexity Check
    private passwordComplexityValidator(control: AbstractControl): ValidationErrors | null {
        const password = control.value;
        if (!password) return null;

        const hasLower = /[a-z]+/.test(password);
        const hasUpper = /[A-Z]+/.test(password);
        const hasDigit = /\d+/.test(password);
        const hasSpecial = /[^a-zA-Z0-9\s]+/.test(password); 

        const isValid = hasLower && hasUpper && hasDigit && hasSpecial;
        
        return isValid ? null : { passwordComplexity: true };
    }

    // US 01: Password Match Check (Applies to the whole form group)
    private passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
        const password = group.get('password')?.value;
        const confirmPassword = group.get('confirmPassword')?.value;

        return password === confirmPassword ? null : { mismatch: true };
    }
    
    // US 01: Gender Check (Male, Female, Others)
    private genderValidator(control: AbstractControl): ValidationErrors | null {
        const gender = control.value;
        if (!gender) return null;
        
        const validGenders = ['Male', 'Female', 'Others'];
        return validGenders.includes(gender) ? null : { genderValid: true };
    }

    // --- FORM SUBMISSION WITH API CALL ---

    onRegister() {
        if (this.registrationForm.invalid) {
            this.registrationForm.markAllAsTouched();
            return;
        }

        this.isLoading = true; // Start loading state
        
        // Ensure the DTO only contains fields the backend expects
        const registrationData: RegistrationDTO = {
            firstName: this.registrationForm.value.firstName,
            surname: this.registrationForm.value.surname,
            emailAddress: this.registrationForm.value.emailAddress,
            dateOfBirth: this.registrationForm.value.dateOfBirth,
            gender: this.registrationForm.value.gender,
            password: this.registrationForm.value.password,
            confirmPassword: this.registrationForm.value.confirmPassword,
        };
        
        this.authService.register(registrationData).subscribe({
            next: (response: UserResponse) => {
                this.isLoading = false;
                
                // SRS Flow: Show confirmation then navigate to login
                alert(`Account Confirmed! Welcome ${response.firstName}. You can now log in.`); 
                this.router.navigate(['/']); // Navigate back to Login page
            },
            error: (err) => {
                this.isLoading = false;
                
                // Display user-friendly error from the centralized exception handler (409 Conflict)
                alert('Registration Failed: ' + err.message); 
            }
        });
    }
}
