import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminHomeComponent } from './components/admin-home/admin-home.component';
import { MaterialModule } from 'src/infrastructure/material.module';
import { InjuriesComponent } from './components/injuries/injuries.component';
import { GameStatsComponent } from './components/game-stats/game-stats.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';



@NgModule({
  declarations: [
    AdminHomeComponent,
    InjuriesComponent,
    GameStatsComponent, 
    
    
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
  ]
})
export class AdminModule { }
