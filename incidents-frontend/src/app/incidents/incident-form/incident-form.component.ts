import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { ApiClient } from '../../core/api-client.service';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-incident-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './incident-form.component.html'
})
export class IncidentFormComponent implements OnInit {
  form = new FormGroup({
    titulo: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(120)]),
    descricao: new FormControl('', [Validators.maxLength(5000)]),
    prioridade: new FormControl('MEDIA', [Validators.required]),
    status: new FormControl('ABERTA', [Validators.required]),
    responsavelEmail: new FormControl('', [Validators.required, Validators.email]),
    tags: new FormControl('')
  });
  id?: string;

  constructor(private api: ApiClient, private router: Router, private route: ActivatedRoute){}

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id') || undefined;
    if (this.id) {
      this.api.getOne(`/incidents/${this.id}`).subscribe((res: any) => {
        this.form.patchValue({ ...res, tags: (res.tags || []).join(',') });
      });
    }
  }

  save() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const raw = this.form.value;
    const body = {
      ...raw,
      tags: raw.tags ? raw.tags.split(',').map((t: string) => t.trim()).filter(Boolean) : []
    };
    const op = this.id ? this.api.put(`/incidents/${this.id}`, body) : this.api.post('/incidents', body);
    op.subscribe(() => this.router.navigate(['/incidents']));
  }
}
