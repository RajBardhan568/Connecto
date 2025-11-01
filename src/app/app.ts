// src/app/app.component.ts

import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './interceptors/jwt.interceptor';


providers: [
  { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
]

@Component({
  selector: 'app-root',
  standalone: true,
  // Import RouterOutlet to display components based on the current URL
  imports: [RouterOutlet, ], 
  template: `
    <router-outlet></router-outlet>
  `,
  styleUrls: ['./app.css']
})




export class AppComponent {
  title = 'Connecto';
}