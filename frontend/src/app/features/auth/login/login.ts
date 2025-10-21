import { Component, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { Toast } from '../../../shared/toast/toast';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, Toast],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  email = '';
  password = '';

  @ViewChild(Toast) toast!: Toast;

  constructor(private auth: AuthService, private router: Router) {}

  onSubmit() {
    const data = { email: this.email, password: this.password };

    this.auth.login(data).subscribe({
      next: () => {
        this.toast.show('Login successful! 🎉', 'success');
        setTimeout(() => this.router.navigate(['/feed/community']), 1500);
      },
      error: (err) => {
        console.error('Login failed:', err);
        this.toast.show('Invalid email or password.', 'error');
      },
    });
  }
}
