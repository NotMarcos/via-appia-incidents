import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ApiClient } from '../../core/api-client.service';
import { Incident } from '../../core/models/incident.model';

@Component({
  selector: 'app-incident-detail',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div *ngIf="incident">
      <h2>{{ incident.titulo }}</h2>
      <p>{{ incident.descricao }}</p>
      <p>Status: {{ incident.status }}</p>
      <p>Prioridade: {{ incident.prioridade }}</p>
      <p>Responsável: {{ incident.responsavelEmail }}</p>
    </div>
  `
})
export class IncidentDetailComponent implements OnInit {
  incident!: Incident | null;

  constructor(
    private api: ApiClient,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.api.getOne<Incident>(`/incidents/${id}`).subscribe(res => {
        this.incident = res;
      });
    }
  }
}
