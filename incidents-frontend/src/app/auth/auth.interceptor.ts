import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const snack = inject(MatSnackBar);

  const token = auth.getToken();

  // Se não tem token, segue a requisição normal
  if (!token) {
    return next(req).pipe(
      catchError((err) => handleError(err, router, snack, auth))
    );
  }

  // Clonar com Authorization header
  const cloned = req.clone({
    setHeaders: { Authorization: `Bearer ${token}` }
  });

  return next(cloned).pipe(
    catchError((err) => handleError(err, router, snack, auth))
  );
};

// -------------------------------------------------------------
// Função de tratamento de erros global
// -------------------------------------------------------------
function handleError(
  error: HttpErrorResponse,
  router: Router,
  snack: MatSnackBar,
  auth: AuthService
) {
  switch (error.status) {
    case 0:
      snack.open('Não foi possível conectar ao servidor.', 'OK', {
        duration: 3000
      });
      break;

    case 400:
      snack.open('Requisição inválida.', 'OK', { duration: 3000 });
      break;

    case 401:
      snack.open('Sessão expirada. Faça login novamente.', 'OK', {
        duration: 3000
      });
      auth.logout();
      router.navigate(['/login']);
      break;

    case 403:
      snack.open('Você não tem permissão para executar essa ação.', 'OK', {
        duration: 3000
      });
      break;

    case 404:
      snack.open('Recurso não encontrado.', 'OK', { duration: 3000 });
      break;

    case 500:
      snack.open('Erro interno no servidor.', 'OK', { duration: 3000 });
      break;

    default:
      snack.open(`Erro: ${error.message}`, 'OK', { duration: 3000 });
  }

  return throwError(() => error);
}
