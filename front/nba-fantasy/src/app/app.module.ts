import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthModule } from './modules/auth/auth.module';
import { LayoutModule } from './modules/layout/layout.module';
import { UnregisteredUserModule } from './modules/unregistered-user/unregistered-user.module';
import { AppRoutingModule } from 'src/infrastructure/app-routing.module';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AdminModule } from './modules/admin/admin.module';
import { MaterialModule } from '../infrastructure/material.module'; 
import { ManagerModule } from './modules/manager/manager.module';
import { AuthInterceptor } from './modules/auth/interceptor/auth-interceptor.interceptor';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AuthModule,
    LayoutModule,
    UnregisteredUserModule,
    MaterialModule,
    AdminModule,
    ManagerModule
  ],
  providers: [
    {
      provide : HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi   : true,
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
