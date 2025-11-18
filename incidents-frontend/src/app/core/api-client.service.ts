import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiClient {
  constructor(private http: HttpClient) {}

  getPage<T>(path: string, paramsObj: any = {}): Observable<any> {
    let params = new HttpParams();
    Object.keys(paramsObj || {}).forEach(k => {
      if (paramsObj[k] !== undefined && paramsObj[k] !== null) params = params.set(k, paramsObj[k]);
    });
    return this.http.get<T>(`${environment.apiBaseUrl}${path}`, { params });
  }

  getOne<T>(path: string) { return this.http.get<T>(`${environment.apiBaseUrl}${path}`); }
  post<T>(path: string, body: any) { return this.http.post<T>(`${environment.apiBaseUrl}${path}`, body); }
  put<T>(path: string, body: any) { return this.http.put<T>(`${environment.apiBaseUrl}${path}`, body); }
  delete<T>(path: string) { return this.http.delete<T>(`${environment.apiBaseUrl}${path}`); }
}
