import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManagerHomeComponent } from './components/manager-home/manager-home.component';
import { MaterialModule } from '../../../infrastructure/material.module';
import { RecommendationComponent } from './components/recommendation/recommendation.component'; // adjust the path as necessary
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    ManagerHomeComponent,
    RecommendationComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class ManagerModule { }
