import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminHomeComponent } from './components/admin-home/admin-home.component';
import { MaterialModule } from 'src/infrastructure/material.module';
import { CategoryScoresComponent } from './components/category-scores/category-scores.component';
import { AddCategoryScoresDialogComponent } from './components/add-category-scores-dialog/add-category-scores-dialog.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AddFilterDialogComponent } from './components/add-filter-dialog/add-filter-dialog.component';
import { InjuriesComponent } from './components/injuries/injuries.component';
import { GameStatsComponent } from './components/game-stats/game-stats.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ReportComponent } from './components/report/report.component';



@NgModule({
  declarations: [
    AdminHomeComponent,
    CategoryScoresComponent,
    AddCategoryScoresDialogComponent,
    AddFilterDialogComponent,
    InjuriesComponent,
    GameStatsComponent,
    ReportComponent,
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class AdminModule { }
