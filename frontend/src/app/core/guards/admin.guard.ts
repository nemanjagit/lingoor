import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { jwtDecode } from 'jwt-decode';

export const AdminGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const token = auth.getToken();

  if (!token) {
    router.navigate(['/login']);
    return false;
  }

  try {
    const decoded: any = jwtDecode(token);
    const roles = decoded?.roles || decoded?.role || [];

    const isAdmin =
      Array.isArray(roles)
        ? roles.includes('ROLE_ADMIN')
        : roles === 'ROLE_ADMIN';

    if (!isAdmin) {
      router.navigate(['/feed/community']);
      return false;
    }
    return true;
  } catch {
    router.navigate(['/login']);
    return false;
  }
};
