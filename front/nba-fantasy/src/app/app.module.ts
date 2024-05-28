import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthModule } from './modules/auth/auth.module';
import { LayoutModule } from './modules/layout/layout.module';
import { UnregisteredUserModule } from './modules/unregistered-user/unregistered-user.module';
import { AppRoutingModule } from 'src/infrastructure/app-routing.module';
import { HttpClientModule } from '@angular/common/http';

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
    UnregisteredUserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
