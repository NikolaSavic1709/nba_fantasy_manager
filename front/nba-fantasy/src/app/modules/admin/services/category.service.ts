import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AddCategoryScores, CategoryScores } from '../model/categoryScore';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private headers = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient) {
  }

  getCategoryScores(): Observable<CategoryScores[]> {
    return this.http.get<CategoryScores[]>(environment.apiHost + 'category_scores');
  }

  activateCategoryScores(id:number): Observable<CategoryScores> {
    return this.http.get<CategoryScores>(environment.apiHost + 'category_scores/activate/'+id.toString());
  }

  addCategoryScores(categoryScore:AddCategoryScores): Observable<CategoryScores> {
    return this.http.post<CategoryScores>(environment.apiHost + "category_scores",
      {
        pointScore: categoryScore.pointScore,
        reboundScore: categoryScore.reboundScore,
        assistScore: categoryScore.assistScore,
        stealScore: categoryScore.stealScore,
        turnoverScore: categoryScore.turnoverScore,
        blockScore: categoryScore.blockScore,
        bonusMargin: categoryScore.bonusMargin
      }, {"headers": this.headers})
  }
}
