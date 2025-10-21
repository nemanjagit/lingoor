import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../models/post';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class PostsService {
  private api = `${environment.apiUrl}/posts`;

  constructor(private http: HttpClient) {}

  // COMMUNITY FEED
  getCommunityFeed(
    query: string = '',
    sort: string = '',
    page: number = 0,
    size: number = 10
  ): Observable<Post[]> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (query) params = params.set('query', query);
    if (sort) params = params.set('sort', sort);

    return this.http.get<Post[]>(`${this.api}`, { params });
  }

  // PERSONALIZED FEED
  getPersonalizedFeed(
    query: string = '',
    sort: string = '',
    page: number = 0,
    size: number = 10
  ): Observable<Post[]> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (query) params = params.set('query', query);
    if (sort) params = params.set('sort', sort);

    return this.http.get<Post[]>(`${this.api}/personalized`, { params });
  }


  toggleLike(postId: number) {
    return this.http.post(`${this.api}/${postId}/likes/toggle`, {});
  }

  getWordOfTheDay(): Observable<Post> {
    return this.http.get<Post>(`${this.api}/word-of-the-day`);
  }

  createPost(data: { word: string; definition: string }) {
    return this.http.post(`${this.api}`, data);
  }


}
