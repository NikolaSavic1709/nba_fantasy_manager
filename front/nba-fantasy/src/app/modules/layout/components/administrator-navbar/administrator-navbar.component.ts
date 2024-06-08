import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/modules/auth/services/auth.service';

@Component({
  selector: 'app-administrator-navbar',
  templateUrl: './administrator-navbar.component.html',
  styleUrls: ['./administrator-navbar.component.scss']
})
export class AdministratorNavbarComponent {
  public selectedPage!: SelectedPage;
  public enumSP=SelectedPage;
  constructor(private router:Router, private authService:AuthService) {
    this.toInjuries();
    this.authService.setUser();
  }
  toHome(){
    this.selectedPage=SelectedPage.HOME;
    this.router.navigate(['/administrator']);
  }

  toReports() {
    this.selectedPage = SelectedPage.REPORTS;
    this.router.navigate(['/administrator/reports']);
  }
  toGameStats(){
    this.selectedPage = SelectedPage.GAME_STATS;
    this.router.navigate(['/administrator/game_stats']);
  }
  toInjuries(){
    this.selectedPage = SelectedPage.INJURIES;
    this.router.navigate(['/administrator/injuries']);
  }

  toCategoryScores() {
    this.selectedPage = SelectedPage.CATEGORY_SCORES;
    this.router.navigate(['/admin/category-scores']);
  }

  logout() {
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
  }
}

enum SelectedPage {
  HOME, REPORTS, GAME_STATS, INJURIES, CATEGORY_SCORES
}
