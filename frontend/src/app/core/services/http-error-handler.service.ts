import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Toast } from '../../shared/toast/toast';

@Injectable({ providedIn: 'root' })
export class HttpErrorHandlerService {
  private toast?: Toast;

  register(toast: Toast) {
    this.toast = toast;
  }

  handleError(error: unknown, context: string = '') {
    let message = 'An unexpected error occurred';

    if (error instanceof HttpErrorResponse) {
      if (!navigator.onLine) {
        message = 'No internet connection';
      } else if (error.status >= 500) {
        message = 'Server error — please try again later';
      } else if (error.status === 403) {
        message = 'You do not have permission for this action';
      } else if (error.status === 404) {
        message = 'Requested resource not found';
      } else if (error.error?.message) {
        message = error.error.message;
      } else {
        message = `Error ${error.status}`;
      }
    }

    if (this.toast) {
      this.toast.show(`${context ? context + ': ' : ''}${message}`, 'error');
    } else {
      console.error('Toast not registered:', message);
    }
  }
}
