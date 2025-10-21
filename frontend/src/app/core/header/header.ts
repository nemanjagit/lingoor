import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  constructor(private auth: AuthService, private router: Router) {}
  userEmail: string | null = null;

  ngOnInit(){
    this.auth.loggedIn$.subscribe(isLogged => {
      this.userEmail = isLogged ? this.auth.getUserEmail() : null;
    });
  }

  get loggedIn(): boolean {
    return this.auth.isLoggedIn();
  }

  get isAdmin(): boolean {
    const token = this.auth.getToken();
    if (!token) return false;

    try {
      const decoded: any = jwtDecode(token);
      const role = decoded?.role || '';
      return role === 'ROLE_ADMIN';
    } catch {
      return false;
    }
  }

  logout() {
    this.userEmail = null;
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
