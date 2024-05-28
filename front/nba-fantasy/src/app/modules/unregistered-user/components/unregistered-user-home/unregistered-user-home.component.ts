import { Component } from '@angular/core';
import { AuthService } from 'src/app/modules/auth/services/auth.service';

@Component({
  selector: 'app-unregistered-user-home',
  templateUrl: './unregistered-user-home.component.html',
  styleUrls: ['./unregistered-user-home.component.scss']
})
export class UnregisteredUserHomeComponent {
  constructor(private authService: AuthService) {
    this.authService.setUser();
  }
}
