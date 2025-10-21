import { Component, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { Toast } from '../../../shared/toast/toast';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, Toast],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {
  email = '';
  username = '';
  password = '';

  @ViewChild(Toast) toast!: Toast;

  constructor(private auth: AuthService, private router: Router) {}

  onSubmit() {
    const data = {
      email: this.email,
      username: this.username,
      password: this.password,
    };

    this.auth.register(data).subscribe({
      next: () => {
        this.toast.show('Welcome to Lingoor! Your account has been created.', 'success');
        setTimeout(() => this.router.navigate(['/feed/community']), 2000);
      },
      error: (err) => {
        console.error('Registration failed:', err);
        this.toast.show('Registration failed. Please try again.', 'error');
      },
    });
  }
}
