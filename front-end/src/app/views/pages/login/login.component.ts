import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { CommonModule, NgStyle } from '@angular/common';
import {
  ButtonDirective,
  CardBodyComponent,
  CardComponent,
  CardGroupComponent,
  ColComponent,
  ContainerComponent,
  FormControlDirective,
  FormDirective,
  InputGroupComponent,
  InputGroupTextDirective,
  RowComponent
} from '@coreui/angular';
import { IconDirective } from '@coreui/icons-angular';
import { UserService } from 'src/app/services/auth/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [
    CommonModule,               // For *ngIf
    ReactiveFormsModule,        // For formGroup, formControlName
    RouterModule,
    ContainerComponent,
    RowComponent,
    ColComponent,
    CardGroupComponent,
    CardComponent,
    CardBodyComponent,
    FormDirective,
    FormControlDirective,
    InputGroupComponent,
    InputGroupTextDirective,
    IconDirective,
    ButtonDirective,
    NgStyle
  ]
})
export class LoginComponent {
  loginForm: FormGroup;
  showPassword = false;

  constructor(private fb: FormBuilder, private router: Router, private userService: UserService) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  onSubmit() {
  if (this.loginForm.invalid) return;

  const { username, password } = this.loginForm.value;

  this.userService.login(username, password).subscribe({
    next: () => {
      // Tokens and role already stored in UserService
      console.log('Login successful');
      this.router.navigate(['/dashboard']); // Navigate after login
    },
    error: (err) => {
      alert(err.message); // Show error if login fails
    }
  });
}
}
