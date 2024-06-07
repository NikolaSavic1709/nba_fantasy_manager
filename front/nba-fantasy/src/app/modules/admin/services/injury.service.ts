import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthService } from '../../auth/services/auth.service';
import { PlayerDetails, PlayerShortInfo } from '../../manager/model/player';
import { Injury, NewInjury } from '../model/injury';
import { PlayerBasicInfo } from '../model/player';

@Injectable({
  providedIn: 'root'
})
export class InjuryService {

  constructor(private http: HttpClient, private authSerice: AuthService) { }

  

  public getCurrentInjuries(): Observable<Injury[]> {
    return this.http.get<Injury[]>(environment.apiHost + 'currentInjuries');
  }
  public getPlayersForAutocomplete(): Observable<PlayerBasicInfo[]> {
    return this.http.get<PlayerBasicInfo[]>(environment.apiHost + 'playersBasic');
  }
  public setRecovery(id: number): Observable<void> {
    return this.http.put<void>(environment.apiHost + 'recovery', null);
  }
  public addInjury(newInjury: NewInjury):Observable<Injury>{
    return this.http.post<Injury>(environment.apiHost + 'injury', newInjury);
  }
}
