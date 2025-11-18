import { Component, signal } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  form = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required])
  });
  loading = signal(false);
  error = signal<string | null>(null);

  constructor(private auth: AuthService, private router: Router){}

  onSubmit(){
    if (this.form.invalid) return;
    this.loading.set(true);
    this.error.set(null);
    const { username, password } = this.form.value as any;
    this.auth.login(username, password).subscribe({
      next: () => { this.loading.set(false); this.router.navigate(['/']); },
      error: (err) => { this.loading.set(false); this.error.set(err?.error?.message || 'Falha no login'); }
    });
  }
}
