import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './components/login/login.component';
import { MaterialModule } from 'src/infrastructure/material.module';
import { ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    LoginComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule
  ]
})
export class AuthModule { }
