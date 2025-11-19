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

  // Aplica header de Authorization se houver token
  const request = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  const handleErrorFn = (error: HttpErrorResponse) =>
    handleError(error, router, snack, auth);

  return next(request).pipe(catchError(handleErrorFn));
};

// ----------------------------------------------------------------
// Tratamento global de erros
// ----------------------------------------------------------------
function handleError(
  error: HttpErrorResponse,
  router: Router,
  snack: MatSnackBar,
  auth: AuthService
) {
  const backendMsg = error?.error?.message ?? error.message;

  switch (error.status) {
    case 0:
      snack.open('Não foi possível conectar ao servidor.', 'OK', { duration: 3000 });
      break;

    case 400:
      snack.open(`Erro de requisição: ${backendMsg}`, 'OK', { duration: 3000 });
      break;

    case 401:
      snack.open('Sessão expirada. Faça login novamente.', 'OK', { duration: 3000 });
      auth.logout();
      router.navigate(['/login']);
      break;

    case 403:
      snack.open('Você não tem permissão para esta ação.', 'OK', { duration: 3000 });
      break;

    case 404:
      snack.open('Recurso não encontrado.', 'OK', { duration: 3000 });
      break;

    case 422:
      snack.open(`Dados inválidos: ${backendMsg}`, 'OK', { duration: 3000 });
      break;

    case 500:
      snack.open('Erro interno no servidor.', 'OK', { duration: 3000 });
      break;

    default:
      snack.open(`Erro: ${backendMsg}`, 'OK', { duration: 3000 });
  }

  return throwError(() => error);
}
