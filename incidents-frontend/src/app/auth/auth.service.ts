import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'incidents_token';

  constructor(private http: HttpClient){}

  login(username: string, password: string) {
    return this.http
      .post<{ token: string }>(`${environment.apiBaseUrl}/auth/login`, { username, password })
      .pipe(
        tap((res: { token: string }) => {
          if (res?.token) localStorage.setItem(this.tokenKey, res.token);
        })
      );
  }


  logout() {
    localStorage.removeItem(this.tokenKey);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
