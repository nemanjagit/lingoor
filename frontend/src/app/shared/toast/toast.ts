import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root' // globally available
})

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toast.html',
  styleUrl: './toast.scss',
})
export class Toast {
  visible = false;
  message = '';
  type: 'success' | 'error' | 'info' = 'info';

  show(message: string, type: 'success' | 'error' | 'info' = 'info') {
    this.message = message;
    this.type = type;
    this.visible = true;

    setTimeout(() => this.hide(), 4000); // auto-hide after 4s
  }

  hide() {
    this.visible = false;
  }
}
