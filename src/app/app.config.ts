import { provideRouter, Routes } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';
import { LoginComponent } from './components/login/login';
import { RegistrationComponent } from './components/registration/registration';
// Import the new components we're about to create
import { HomeComponent } from './components/main/home/home'; 
import { FriendsComponent } from './components/main/friends/friends';
import { MainShellComponent } from './components/main-shell/main-shell';
import { ProfileComponent } from './components/main/profile/profile'; 
import { FriendProfileComponent } from './components/main/friend-profile/friend-profile'; 

const APP_ROUTES: Routes = [
    // Public Routes
    { path: 'login', component: LoginComponent, title: 'Connecto - Login' },
    { path: 'register', component: RegistrationComponent, title: 'Connecto - Sign Up' },
    { path: '', redirectTo: 'login', pathMatch: 'full' }, 

    // Protected Routes (Post-Login Shell)
    { 
        path: 'main', 
        component: MainShellComponent, 
        // canActivate: [AuthGuard], // Security layer added in later phase
        children: [
            { path: 'home', component: HomeComponent, title: 'Connecto - Home' }, 
            { path: 'friends', component: FriendsComponent, title: 'Connecto - Friends' }, 
            { path: 'profile', component: ProfileComponent, title: 'Connecto - Your Profile' }, // US 04
            { path: 'profile/:id', component: FriendProfileComponent, title: 'Connecto - Friend Profile' }, // US 07
            { path: '', redirectTo: 'home', pathMatch: 'full' }
        ]
    },

    { path: '**', redirectTo: 'login' } 
];

export const appConfig: ApplicationConfig = {
    providers: [
        provideRouter(APP_ROUTES),
        provideHttpClient()
    ]
};


// src/app/app.config.ts (Revised to simplify routing and remove nested structure)

// import { provideRouter, Routes } from '@angular/router';
// import { provideHttpClient } from '@angular/common/http';
// import { ApplicationConfig } from '@angular/core';
// import { LoginComponent } from './components/login/login';
// import { RegistrationComponent } from './components/registration/registration';
// import { MainShellComponent } from './components/main-shell/main-shell';
// import { HomeComponent } from './components/main/home/home';
// import { FriendsComponent } from './components/main/friends/friends'; 
// import { ProfileComponent } from './components/main/profile/profile'; 
// import { FriendProfileComponent } from './components/main/friend-profile/friend-profile'; 

// const APP_ROUTES: Routes = [
//     // Public Routes
//     { path: 'login', component: LoginComponent, title: 'Connecto - Login' },
//     { path: 'register', component: RegistrationComponent, title: 'Connecto - Sign Up' },
    
//     // Default Route (We will redirect the base path to the Main Shell for easy design review)
//     { path: '', redirectTo: 'home', pathMatch: 'full' }, 

//     // --- MAIN SHELL ROUTES (For Design Review) ---
//     // We will place the MainShell on the path 'home' itself. 
//     // This allows the full shell (navbar) to load, and the internal RouterOutlet will default to the Home page.
//     { 
//         path: '', 
//         component: MainShellComponent, 
//         children: [
//             // Home/News Feed (Landing page after login)
//             { path: 'home', component: HomeComponent, title: 'Connecto - Home' }, 
            
//             // Friends Management
//             { path: 'friends', component: FriendsComponent, title: 'Connecto - Friends' }, 
            
//             // User's Own Profile
//             { path: 'profile', component: ProfileComponent, title: 'Connecto - Your Profile' }, 
            
//             // Viewing a Friend's Profile
//             { path: 'profile/:id', component: FriendProfileComponent, title: 'Connecto - Friend Profile' },
            
//             // Redirect the '/main' base path if needed (but 'home' is cleaner)
//             { path: 'main', redirectTo: 'home', pathMatch: 'full' }
//         ]
//     },

//     // Catch-all route
//     { path: '**', redirectTo: 'home' } 
// ];

// export const appConfig: ApplicationConfig = {
//     providers: [
//         provideRouter(APP_ROUTES),
//         provideHttpClient()
//     ]
// };