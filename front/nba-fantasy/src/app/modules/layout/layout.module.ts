import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './components/navbar/navbar.component';
import { MaterialModule } from 'src/infrastructure/material.module';
import { AppRoutingModule } from 'src/infrastructure/app-routing.module';
import { UnregisteredUserNavbarComponent } from './components/unregistered-user-navbar/unregistered-user-navbar.component';



@NgModule({
  declarations: [
    NavbarComponent,
    UnregisteredUserNavbarComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    AppRoutingModule
  ],
  exports: [
    NavbarComponent
  ]
})
export class LayoutModule { }
