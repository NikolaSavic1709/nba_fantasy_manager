import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from 'src/app/modules/auth/components/login/login.component';
import { UnregisteredUserHomeComponent } from 'src/app/modules/unregistered-user/components/unregistered-user-home/unregistered-user-home.component';

const routes: Routes = [
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {
    path: 'home',
    component: UnregisteredUserHomeComponent
  },
  {
    path: 'login',
    component: LoginComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
