import { Injectable } from '@angular/core';
import { ApiClient } from '../core/api-client.service';
import { Observable } from 'rxjs';
import { Comment } from '../core/models/comment.model';

@Injectable({ providedIn: 'root' })
export class CommentService {

  constructor(private api: ApiClient) {}

  list(incidentId: string): Observable<Comment[]> {
    return this.api.getOne<Comment[]>(`/incidents/${incidentId}/comments`);
  }

  create(incidentId: string, payload: { autor: string; mensagem: string }): Observable<Comment> {
    return this.api.post<Comment>(`/incidents/${incidentId}/comments`, payload);
  }
}
