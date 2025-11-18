import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiClient {

  constructor(private http: HttpClient) {}

  private buildParams(paramsObj: any): HttpParams {
    let params = new HttpParams();
    Object.keys(paramsObj || {}).forEach(k => {
      const val = paramsObj[k];
      if (val !== undefined && val !== null && val !== '') {
        params = params.set(k, val);
      }
    });
    return params;
  }

  // LISTA COM PAGINAÇÃO
  getPage<T>(path: string, paramsObj: any = {}): Observable<T> {
    return this.http.get<T>(
      `${environment.apiBaseUrl}${path}`,
      { params: this.buildParams(paramsObj) }
    );
  }

  getOne<T>(path: string): Observable<T> {
    return this.http.get<T>(`${environment.apiBaseUrl}${path}`);
  }

  post<T>(path: string, body: any): Observable<T> {
    return this.http.post<T>(`${environment.apiBaseUrl}${path}`, body);
  }

  put<T>(path: string, body: any): Observable<T> {
    return this.http.put<T>(`${environment.apiBaseUrl}${path}`, body);
  }

  delete<T>(path: string): Observable<T> {
    return this.http.delete<T>(`${environment.apiBaseUrl}${path}`);
  }
}
