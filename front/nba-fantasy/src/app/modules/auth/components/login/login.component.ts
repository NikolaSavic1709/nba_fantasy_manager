import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import {Credentials} from "../../model/credentials";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  hide = true;
  submitted = false;
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.email, Validators.required]),
    password: new FormControl('', [Validators.minLength(6), Validators.required])
  });
  hasError = false;
  error = '';

  constructor(private authService: AuthService,
    private router: Router
    ) {

    this.authService.setUser();
  }
  
  
  ngAfterContentInit(): void {
    this.hasError = false;
    this.error = '';
  }

  ngOnInit(): void {
    this.authService.hasErrorObs.subscribe((value) => {
      this.hasError = value;
    })

    this.authService.errorObs.subscribe((value) => {
      this.error = value;
    })

  }

  login() {
    this.submitted = true;
    const login: Credentials = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    }

    if (this.loginForm.valid) {
      this.authService.loginUserObs(login);
    }
  }

  toForgot() {
    this.router.navigate(['/forgot-password']);
  }

  toSignup() {
    this.router.navigate(['/register']);
  }
}
