import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import {environment} from '../../../../environments/environment';


@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reports.html',
  styleUrl: './reports.scss',
})
export class Reports {
  private api = `${environment.apiUrl}/admin/reports`;

  constructor(private http: HttpClient) {}

  downloadReport(type: string) {
    this.http
      .get(`${this.api}/${type}`, { responseType: 'blob' })
      .subscribe({
        next: (blob) => {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `${type}.pdf`;
          a.click();
          window.URL.revokeObjectURL(url);
        },
        error: () => alert('Failed to download report'),
      });
  }
}
