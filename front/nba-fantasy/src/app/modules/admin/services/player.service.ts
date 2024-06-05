import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {Player} from '../model/player';
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PlayerService {

  constructor(private http: HttpClient) {
  }


  getRecommendationList(): Observable<Player[]> {
    return this.http.get<Player[]>(environment.apiHost + 'recommendation_list');
  }
}
