import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostsService } from '../../../core/services/posts.service';
import { AuthService } from '../../../core/services/auth.service';
import { Toast } from '../../../shared/toast/toast';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-post-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './post-card.html',
  styleUrl: './post-card.scss',
})
export class PostCard {
  @Input() post: any;
  @Output() postDeleted = new EventEmitter<void>();
  @Output() wotdSet = new EventEmitter<void>();
  likeInProgress = false;
  followPopupOpen = false;
  showActions = false;
  isAdmin = false;
  isAuthor = false;
  private api = `${environment.apiUrl}`;

  constructor(private posts: PostsService, private http: HttpClient, private auth: AuthService, private toast: Toast) {}

  ngOnInit() {
    const role = this.auth.getRole();
    const userId = this.auth.getUserId();

    this.isAdmin = role === 'ROLE_ADMIN';
    this.isAuthor = userId === this.post.authorId;
  }

  toggleLike() {
    if (this.likeInProgress) return;
    this.likeInProgress = true;

    this.posts.toggleLike(this.post.id).subscribe({
      next: (res: any) => {
        this.post.likedByMe = !this.post.likedByMe;
        this.post.likeCount += this.post.likedByMe ? 1 : -1;
        this.likeInProgress = false;
      },
      error: () => (this.likeInProgress = false),
    });
  }

  toggleFollow() {
    this.http.post(`${this.api}/users/${this.post.authorId}/follow`, {}).subscribe({
      next: (res: any) => {
        this.post.followingAuthor = res.following;
        this.followPopupOpen = false;
      },
      error: (err) => {
        console.error('Follow toggle failed:', err);
      },
    });
  }

  openFollowPopup() {
    this.followPopupOpen = !this.followPopupOpen;
  }

  deletePost() {
    if (!confirm('Are you sure you want to delete this post?')) return;

    this.http.delete(`${this.api}/posts/${this.post.id}`).subscribe({
      next: () => {
        this.toast.show('Post deleted successfully', 'success');
        this.postDeleted.emit(); // tell parent to refresh
      },
      error: (err) => {
        console.error('Failed to delete post', err);
        this.toast.show('Failed to delete post', 'error');
      },
    });
  }

  setWordOfTheDay() {
    this.http.post(`${this.api}/admin/word-of-the-day/${this.post.id}`, {}).subscribe({
      next: () => {
        this.toast.show('Word of the day set!', 'success');
        this.wotdSet.emit(); // tell parent to refresh
      },
      error: (err) => {
        console.error('Failed to set WOTD', err);
        this.toast.show('Failed to set Word of the Day', 'error');
      },
    });
  }

}
