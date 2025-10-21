import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-feed-filters',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './feed-filters.html',
  styleUrl: './feed-filters.scss',
})
export class FeedFilters {
  search = '';
  sort = 'newest'; // default
  typingTimer: any;

  @Output() filtersChanged = new EventEmitter<{ search: string; sort: string }>();

  // Trigger search automatically while typing
  onSearchChange() {
    clearTimeout(this.typingTimer);
    this.typingTimer = setTimeout(() => {
      this.filtersChanged.emit({ search: this.search.trim(), sort: this.sort });
    }, 400);
  }

  onSortChange() {
    this.filtersChanged.emit({ search: this.search.trim(), sort: this.sort });
  }
}
