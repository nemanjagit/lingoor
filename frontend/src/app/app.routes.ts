import { Routes } from '@angular/router';
import { CommunityFeed } from './features/feed/community-feed/community-feed';
import { PersonalizedFeed } from './features/feed/personalized-feed/personalized-feed';
import { Reports } from './features/admin/reports/reports';
import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { AuthGuard } from './core/guards/auth.guard';
import { AdminGuard } from './core/guards/admin.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'feed/community',
    pathMatch: 'full'
  },
  {
    path: 'feed/community',
    component: CommunityFeed
  },
  {
    path: 'feed/personalized',
    component: PersonalizedFeed,
    canActivate: [AuthGuard],
  },
  {
    path: 'admin/reports',
    component: Reports,
    canActivate: [AdminGuard],
  },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: '**', redirectTo: 'feed/community' },
];
