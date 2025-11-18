import { Component, OnInit } from '@angular/core';
import { ApiClient } from '../../core/api-client.service';
import { Incident } from '../../core/models/incident.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormControl } from '@angular/forms';

@Component({
  selector: 'app-incident-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './incident-list.component.html'
})
export class IncidentListComponent implements OnInit {
  incidents: Incident[] = [];
  page = 0;
  size = 10;
  total = 0;
  q = new FormControl('');
  statusFilter = new FormControl('');
  loading = false;

  constructor(private api: ApiClient) {}

  ngOnInit(): void { this.load(); }

  load() {
    this.loading = true;
    const params = {
      page: this.page,
      size: this.size,
      q: this.q.value,
      status: this.statusFilter.value,
      sort: 'dataAbertura,desc'
    };
    this.api.getPage('/incidents', params).subscribe({
      next: (res: any) => {
        this.incidents = res.content || res; // dependendo do backend Page<T> ou lista
        this.total = res.totalElements ?? (this.incidents.length || 0);
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  nextPage() { this.page++; this.load(); }
  prevPage() { if (this.page>0) { this.page--; this.load(); } }

  delete(id: string) {
    if (!confirm('Confirma exclusão?')) return;
    this.api.delete(`/incidents/${id}`).subscribe(() => this.load());
  }
}
