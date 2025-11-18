import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface IncidentResponse {
  id: string;
  titulo: string;
  descricao: string;
  prioridade: string;
  status: string;
  responsavelEmail: string;
  tags: string[];
  dataAbertura: string;
  dataAtualizacao: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

@Injectable({ providedIn: 'root' })
export class IncidentService {
  private api = 'http://localhost:8080/incidents';

  constructor(private http: HttpClient) {}

  list(
    page = 0,
    size = 10,
    q = '',
    status = '',
    prioridade = ''
  ): Observable<Page<IncidentResponse>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('q', q)
      .set('status', status)
      .set('prioridade', prioridade);

    return this.http.get<Page<IncidentResponse>>(this.api, { params });
  }

  delete(id: string) {
    return this.http.delete(`${this.api}/${id}`);
  }

  createIncident(data: any) {
    return this.http.post<IncidentResponse>(this.api, data);
  }

  updateIncident(id: string, data: any) {
    return this.http.put<IncidentResponse>(`${this.api}/${id}`, data);
  }

  getIncident(id: string) {
    return this.http.get<IncidentResponse>(`${this.api}/${id}`);
  }

}


