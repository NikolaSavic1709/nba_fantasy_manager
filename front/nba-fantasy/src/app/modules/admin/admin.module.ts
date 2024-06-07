import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminHomeComponent } from './components/admin-home/admin-home.component';
import { MaterialModule } from 'src/infrastructure/material.module';
import { CategoryScoresComponent } from './components/category-scores/category-scores.component';
import { AddCategoryScoresDialogComponent } from './components/add-category-scores-dialog/add-category-scores-dialog.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AddFilterDialogComponent } from './components/add-filter-dialog/add-filter-dialog.component';



@NgModule({
  declarations: [
    AdminHomeComponent,
    CategoryScoresComponent,
    AddCategoryScoresDialogComponent,
    AddFilterDialogComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class AdminModule { }
