import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

import { IncidentService, IncidentResponse } from '../../services/incident.service';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-incident-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,

    // Material
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatCardModule
  ],
  templateUrl: './incident-form.component.html',
  styleUrls: ['./incident-form.component.scss']
})
export class IncidentFormComponent implements OnInit {

  form!: FormGroup;
  id: string | null = null;
  isEditing = false;
  loading = false;

  prioridades = ['BAIXA', 'MEDIA', 'ALTA'];
  statusList = ['ABERTA', 'EM_ANDAMENTO', 'RESOLVIDA', 'CANCELADA'];

  constructor(
    private fb: FormBuilder,
    private service: IncidentService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      titulo: ['', [Validators.required, Validators.minLength(5)]],
      descricao: [''],
      prioridade: ['', Validators.required],
      status: ['', Validators.required],
      responsavelEmail: ['', [Validators.required, Validators.email]],
      tags: ['']
    });

    this.id = this.route.snapshot.paramMap.get('id');
    this.isEditing = !!this.id;

    if (this.isEditing) {
      this.loadData();
    }
  }

  loadData() {
    this.service.getIncident(this.id!).subscribe({
      next: (res: IncidentResponse) => {
        this.form.patchValue({
          titulo: res.titulo,
          descricao: res.descricao,
          prioridade: res.prioridade,
          status: res.status,
          responsavelEmail: res.responsavelEmail,
          tags: res.tags.join(', ')
        });
      }
    });
  }

  submit() {
    if (this.form.invalid) return;

    const payload = {
      ...this.form.value,
      tags: this.form.value.tags
        .split(',')
        .map((t: string) => t.trim())
        .filter((t: string) => t.length > 0)
    };

    this.loading = true;

    if (this.isEditing) {
      this.service.updateIncident(this.id!, payload).subscribe({
        next: () => {
          this.loading = false;
          this.router.navigate(['/incidents']);
        },
        error: () => (this.loading = false)
      });
    } else {
      this.service.createIncident(payload).subscribe({
        next: () => {
          this.loading = false;
          this.router.navigate(['/incidents']);
        },
        error: () => (this.loading = false)
      });
    }
  }
}
