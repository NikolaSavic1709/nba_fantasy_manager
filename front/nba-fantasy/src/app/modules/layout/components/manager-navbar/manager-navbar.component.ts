import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/modules/auth/services/auth.service';

@Component({
  selector: 'app-manager-navbar',
  templateUrl: './manager-navbar.component.html',
  styleUrls: ['./manager-navbar.component.scss']
})
export class ManagerNavbarComponent {
  public selectedPage!: SelectedPage;
  public enumSP = SelectedPage;

  constructor(private router: Router, private authService: AuthService) {
    this.toProfile();
    this.authService.setUser();
  }


  toProfile() {
    console.log("profile");
    this.selectedPage = SelectedPage.PROFILE;
    this.router.navigate(['/manager']);
  }

  logout() {
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
  }


  toStats() {
    this.selectedPage = SelectedPage.STATS;
    const id = this.authService.getId();
    this.router.navigate(['/statistics/manager/' + id.toString()]);
  }
}

enum SelectedPage {
  STATS, PROFILE
}
