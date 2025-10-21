import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './core/header/header';
import { Toast } from './shared/toast/toast';
import { ConfirmDialog } from './shared/confirm-dialog/confirm-dialog';
import { HttpErrorHandlerService } from './core/services/http-error-handler.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Header, Toast, ConfirmDialog],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class AppComponent implements AfterViewInit {
  @ViewChild(Toast) toast!: Toast;

  constructor(private errorHandler: HttpErrorHandlerService) {}

  ngAfterViewInit() {
    this.errorHandler.register(this.toast);
  }
}
