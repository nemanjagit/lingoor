import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Post } from '../../../core/models/post';
import { PostsService } from '../../../core/services/posts.service';
import { PostCard } from '../post-card/post-card';
import { WordOfTheDay } from '../word-of-the-day/word-of-the-day';
import { FeedFilters } from '../feed-filters/feed-filters';
import { CreatePost } from '../create-post/create-post';

@Component({
  selector: 'app-community-feed',
  standalone: true,
  imports: [CommonModule, PostCard, WordOfTheDay, FeedFilters, CreatePost],
  templateUrl: './community-feed.html',
  styleUrl: './community-feed.scss',
})
export class CommunityFeed implements OnInit {
  posts: Post[] = [];
  currentQuery = '';
  currentSort = 'newest';
  currentPage = 0;
  pageSize = 10;
  loading = false;
  allLoaded = false;

  constructor(private postsService: PostsService) {}

  ngOnInit() {
    this.loadPosts();
  }

  loadPosts(reset: boolean = false) {
    if (this.loading) return;
    this.loading = true;

    const pageToLoad = reset ? 0 : this.currentPage;

    this.postsService
      .getCommunityFeed(this.currentQuery, this.currentSort, pageToLoad, this.pageSize)
      .subscribe({
        next: (res) => {
          if (reset) this.posts = res;
          else this.posts = [...this.posts, ...res];

          this.allLoaded = res.length < this.pageSize; // No more posts
          this.currentPage = pageToLoad + 1;
          this.loading = false;
        },
        error: (err) => {
          console.error('Failed to load posts', err);
          this.loading = false;
        },
      });
  }

  onFiltersChanged(filters: { search: string; sort: string }) {
    this.currentQuery = filters.search;
    this.currentSort = filters.sort;
    this.currentPage = 0;
    this.allLoaded = false;
    this.loadPosts(true); // reset mode
  }

  loadMore() {
    this.loadPosts(); // load next page
  }

  onPostDeleted() {
    this.currentPage = 0;
    this.loadPosts(true);
  }
}
