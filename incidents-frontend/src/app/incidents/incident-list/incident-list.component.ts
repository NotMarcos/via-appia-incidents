import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { buildQueryParams } from '../../shared/utils/query-utils';


import { IncidentService, IncidentResponse } from '../../services/incident.service';

@Component({
  selector: 'app-incident-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    // Material
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatInputModule,
  ],
  templateUrl: './incident-list.component.html',
  styleUrls: ['./incident-list.component.scss']
})
export class IncidentListComponent implements OnInit {

  displayedColumns = ['titulo', 'prioridade', 'status', 'responsavelEmail', 'acoes'];
  data: IncidentResponse[] = [];

  // filtros
  q: string = '';
  status: string = '';
  prioridade: string = '';

  totalElements = 0;
  page = 0;
  size = 10;

  constructor(
    private incidentService: IncidentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.load();
  }

load() {
  const params = buildQueryParams(this.page, this.size, this.q, this.status, this.prioridade);

  this.incidentService.list(
    params.page,
    params.size,
    params.q,
    params.status,
    params.prioridade
  ).subscribe(res => {
    this.data = res.content;
    this.totalElements = res.totalElements;
  });
}


  onPage(event: PageEvent) {
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.load();
  }

  clearFilters() {
    this.q = '';
    this.status = '';
    this.prioridade = '';
    this.page = 0;
    this.load();
  }

  create() {
    this.router.navigate(['/incidents/new']);
  }

  view(id: string) {
    this.router.navigate(['/incidents', id]);
  }

  edit(id: string) {
    this.router.navigate(['/incidents', id, 'edit']);
  }
  remove(id: string) {
    if (!confirm('Deseja realmente remover este incidente?')) return;

    this.incidentService.delete(id).subscribe(() => {
      this.load();
    });
  }
}
