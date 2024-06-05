import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManagerHomeComponent } from './components/manager-home/manager-home.component';
import { MaterialModule } from '../../../infrastructure/material.module'; // adjust the path as necessary


@NgModule({
  declarations: [
    ManagerHomeComponent
  ],
  imports: [
    CommonModule,
    MaterialModule
  ]
})
export class ManagerModule { }
