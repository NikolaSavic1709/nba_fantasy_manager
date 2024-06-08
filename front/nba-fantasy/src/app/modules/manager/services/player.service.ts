import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import { Observable } from 'rxjs';
import { PlayerDetails, PlayerShortInfo, RecommendationAndPreferencesList } from '../model/player';
import { environment } from 'src/environments/environment';
import { AuthService } from '../../auth/services/auth.service';
import { Player } from '../../admin/model/player';

@Injectable({
  providedIn: 'root'
})
export class PlayerService {

  constructor(private http: HttpClient, private authSerice: AuthService) { }

  getRecommendationList(): Observable<Player[]> {
    return this.http.get<Player[]>(environment.apiHost + 'recommendation_list');
  }

  public getRecommendation(): Observable<RecommendationAndPreferencesList>{
    return this.http.get<RecommendationAndPreferencesList>(environment.apiHost + 'recommend');
  }

  public getPlayerDetails(id: number): Observable<PlayerDetails> {
    
    return this.http.get<PlayerDetails>(environment.apiHost + 'playerDetails/'+id);
  }

  public getPlayers(): Observable<PlayerShortInfo[]> {
      let id=this.authSerice.getId();
      return this.http.get<PlayerShortInfo[]>(environment.apiHost + 'fantasyTeamPlayers/'+id);

  }
  public addPlayer(id: number): Observable<PlayerDetails> {
    
    return this.http.put<PlayerDetails>(environment.apiHost + 'addPlayer/'+id, null);
  }
  public removePlayer(id: number): Observable<void> {
    console.log("lalala")
    return this.http.put<void>(environment.apiHost + 'removePlayer/'+id, null);
  }
}
