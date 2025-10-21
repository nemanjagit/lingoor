import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';
import { environment } from '../../../environments/environment';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private api = `${environment.apiUrl}`;
  private tokenKey = 'lingoor_token';
  private loggedInSubject = new BehaviorSubject<boolean>(this.isLoggedIn());

  loggedIn$ = this.loggedInSubject.asObservable();

  constructor(private http: HttpClient) {}

  register(data: { email: string; username: string; password: string }) {
    return this.http.post<{ jwt: string }>(`${this.api}/register`, data).pipe(
      tap(res => localStorage.setItem(this.tokenKey, res.jwt))
    );
  }

  login(data: { email: string; password: string }) {
    return this.http.post<{ jwt: string }>(`${this.api}/login`, data).pipe(
      tap(res => {
        localStorage.setItem(this.tokenKey, res.jwt);
        this.loggedInSubject.next(true); // notify components for email loading
      })
    );
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    this.loggedInSubject.next(false); // notify components for email loading
  }

  getToken() {
    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn() {
    return !!this.getToken();
  }

  getRole(): string | null {
    const token = this.getToken();
    if (!token) return null;
    const decoded: any = jwtDecode(token);
    return decoded.role || null;
  }

  getUserId(): number | null {
    const token = this.getToken();
    if (!token) return null;
    const decoded: any = jwtDecode(token);
    return decoded.userId || null;
  }

  getUserEmail(): string | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      const decoded: any = jwtDecode(token);
      return decoded.username || decoded.sub || null;
    } catch {
      return null;
    }
  }

}
