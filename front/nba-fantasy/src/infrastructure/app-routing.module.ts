import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminHomeComponent } from 'src/app/modules/admin/components/admin-home/admin-home.component';
import { GameStatsComponent } from 'src/app/modules/admin/components/game-stats/game-stats.component';
import { InjuriesComponent } from 'src/app/modules/admin/components/injuries/injuries.component';
import { LoginComponent } from 'src/app/modules/auth/components/login/login.component';
import { ManagerHomeComponent } from 'src/app/modules/manager/components/manager-home/manager-home.component';
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
  },
  {
    path: 'manager',
    component: ManagerHomeComponent
  },
  {
    path: 'administrator',
    component: AdminHomeComponent
  },
  {
    path: 'administrator/injuries',
    component: InjuriesComponent
  },
  {
    path: 'administrator/game_stats',
    component: GameStatsComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
