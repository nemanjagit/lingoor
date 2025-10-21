import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './confirm-dialog.html',
  styleUrl: './confirm-dialog.scss',
})
export class ConfirmDialog {
  @Output() confirmed = new EventEmitter<boolean>();
  visible = false;
  title = '';
  message = '';

  show(title: string, message: string) {
    this.title = title;
    this.message = message;
    this.visible = true;
  }

  cancel() {
    this.visible = false;
    this.confirmed.emit(false);
  }

  confirm() {
    this.visible = false;
    this.confirmed.emit(true);
  }
}
