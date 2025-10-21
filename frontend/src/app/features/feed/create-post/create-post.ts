import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PostsService } from '../../../core/services/posts.service';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-post.html',
  styleUrl: './create-post.scss',
})
export class CreatePost {
  @Output() postCreated = new EventEmitter<void>();

  showModal = false;
  word = '';
  definition = '';
  loading = false;

  constructor(private postsService: PostsService) {}

  toggleModal() {
    this.showModal = !this.showModal;
  }

  createPost() {
    if (!this.word.trim() || !this.definition.trim()) return;

    this.loading = true;

    this.postsService.createPost({ word: this.word.trim(), definition: this.definition.trim() })
      .subscribe({
        next: () => {
          this.loading = false;
          this.word = '';
          this.definition = '';
          this.showModal = false;
          this.postCreated.emit(); // Notify parent to reload feed
        },
        error: (err) => {
          console.error('Failed to create post', err);
          this.loading = false;
        }
      });
  }
}
