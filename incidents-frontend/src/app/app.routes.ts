import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { IncidentListComponent } from './incidents/incident-list/incident-list.component';
import { IncidentFormComponent } from './incidents/incident-form/incident-form.component';
import { IncidentDetailComponent } from './incidents/incident-detail/incident-detail.component';
import { AuthGuard } from './auth/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: 'incidents', pathMatch: 'full' },
  { path: 'incidents', component: IncidentListComponent, canActivate: [AuthGuard] },
  { path: 'incidents/new', component: IncidentFormComponent, canActivate: [AuthGuard] },
  { path: 'incidents/:id/edit', component: IncidentFormComponent, canActivate: [AuthGuard] },
  { path: 'incidents/:id', component: IncidentDetailComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: 'incidents' }
];
