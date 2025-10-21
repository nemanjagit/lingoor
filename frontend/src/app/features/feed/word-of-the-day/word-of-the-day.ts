import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostsService } from '../../../core/services/posts.service';

@Component({
  selector: 'app-word-of-the-day',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './word-of-the-day.html',
  styleUrl: './word-of-the-day.scss',
})
export class WordOfTheDay implements OnInit {
  wordOfTheDay: any = null;
  hasWord = false;

  constructor(private posts: PostsService) {}

  ngOnInit() {
    this.posts.getWordOfTheDay().subscribe({
      next: (res) => {
        this.wordOfTheDay = res;
        this.hasWord = true;
      },
      error: () => {
        this.hasWord = false;
      },
    });
  }
}
