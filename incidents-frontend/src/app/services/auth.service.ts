import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap, map } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private api = `${environment.apiBaseUrl}/auth`;

  constructor(private http: HttpClient) {}

  // -------------------------------------------------------------
  // LOGIN
  // -------------------------------------------------------------
  login(username: string, password: string) {
    return this.http.post<{ token: string }>(`${this.api}/login`, { username, password })
      .pipe(
        tap(res => this.storeToken(res.token)),
        map(res => this.decodeToken(res.token))   // devolve claims para o componente
      );
  }

  // -------------------------------------------------------------
  // TOKEN STORAGE
  // -------------------------------------------------------------
  private storeToken(token: string) {
    localStorage.setItem('token', token);
  }

  logout(redirect: boolean = false) {
    localStorage.removeItem('token');
    if (redirect) {
      // redirecionamento opcional
      window.location.href = '/login';
    }
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // -------------------------------------------------------------
  // JWT DECODE + expiração
  // -------------------------------------------------------------
  decodeToken(token: string): any | null {
    try {
      return jwtDecode(token);
    } catch {
      return null;
    }
  }

  getUser(): any | null {
    const token = this.getToken();
    if (!token) return null;
    return this.decodeToken(token);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) return false;

    const decoded = this.decodeToken(token);
    if (!decoded) {
      this.logout();
      return false;
    }

    // exp em segundos → converter para ms
    if (decoded.exp * 1000 < Date.now()) {
      this.logout();
      return false;
    }

    return true;
  }
}
