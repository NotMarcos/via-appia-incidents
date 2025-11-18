import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

import { ApiClient } from '../../core/api-client.service';
import { Incident } from '../../core/models/incident.model';
import { Comment } from '../../core/models/comment.model';

import { CommentService } from '../../services/comment.service';

// Angular Material
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { AppDatePipe } from '../../shared/pipes/app-date.pipe';

@Component({
  selector: 'app-incident-detail',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatDividerModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    AppDatePipe
  ],
  templateUrl: './incident-detail.component.html',
  styleUrls: ['./incident-detail.component.scss']
})
export class IncidentDetailComponent implements OnInit {

  incident: Incident | null = null;
  comments: Comment[] = [];

  id!: string;
  newComment: string = '';

  constructor(
    private api: ApiClient,
    private commentService: CommentService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.id = params['id'];

      this.loadIncident();
      this.loadComments();
    });
  }

  loadIncident() {
    this.api.getOne<Incident>(`/incidents/${this.id}`).subscribe({
      next: res => this.incident = res
    });
  }

  loadComments() {
    this.commentService.list(this.id).subscribe({
      next: res => this.comments = res
    });
  }

  addComment() {
    if (!this.newComment.trim()) return;

    const payload = {
      autor: 'Usuário',
      mensagem: this.newComment
    };

    this.commentService.create(this.id, payload).subscribe({
      next: (created) => {
        this.comments.unshift(created); 
        this.newComment = '';
      }
    });
  }


  goBack() {
    this.router.navigate(['/incidents']);
  }

  edit(id: string) {
    this.router.navigate([`/incidents/${id}/edit`]);
  }
}
