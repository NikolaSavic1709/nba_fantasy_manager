import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CategoryScores } from '../model/categoryScore';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(private http: HttpClient) {
  }

  getCategoryScores(): Observable<CategoryScores[]> {
    return this.http.get<CategoryScores[]>(environment.apiHost + 'category_scores');
  }

  activateCategoryScores(id:number): Observable<CategoryScores> {
    return this.http.get<CategoryScores>(environment.apiHost + 'category_scores/activate/'+id.toString());
  }
}
