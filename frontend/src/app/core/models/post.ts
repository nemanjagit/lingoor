export interface Post {
  id: number;
  word: string;
  definition: string;
  author: string;
  authorId: number;
  createdAt: string;
  likesCount: number;
  likedByMe: boolean;
  followingAuthor: boolean;
}
