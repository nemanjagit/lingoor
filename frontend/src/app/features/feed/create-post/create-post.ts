import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PostsService } from '../../../core/services/posts.service';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-post.html',
  styleUrl: './create-post.scss',
})
export class CreatePost {
  @Output() postCreated = new EventEmitter<void>();

  showModal = false;
  loading = false;
  form: FormGroup;

  constructor(private fb: FormBuilder, private postsService: PostsService) {
    this.form = this.fb.group({
      word: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      definition: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(300)]],
    });
  }

  toggleModal() {
    this.showModal = !this.showModal;
  }

  createPost() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { word, definition } = this.form.value;
    this.loading = true;

    this.postsService.createPost({ word: word.trim(), definition: definition.trim() }).subscribe({
      next: () => {
        this.loading = false;
        this.form.reset();
        this.showModal = false;
        this.postCreated.emit();
      },
      error: (err) => {
        console.error('Failed to create post', err);
        this.loading = false;
      },
    });
  }
}
