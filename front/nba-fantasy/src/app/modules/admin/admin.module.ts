import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminHomeComponent } from './components/admin-home/admin-home.component';
import { MaterialModule } from 'src/infrastructure/material.module';
import { CategoryScoresComponent } from './components/category-scores/category-scores.component';



@NgModule({
  declarations: [
    AdminHomeComponent,
    CategoryScoresComponent
  ],
  imports: [
    CommonModule,
    MaterialModule
  ]
})
export class AdminModule { }
