import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Player } from '../model/player';
import { environment } from 'src/environments/environment';
import { TeamSelection } from '../model/teamSelection';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private headers = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient) {
  }

  getMostSelectedPlayersByThreshold(threshold:number): Observable<Player[]> {
    const url = `${environment.apiHost}selections/threshold?threshold=${threshold.toString()}`;
    return this.http.get<Player[]>(url);
  }

  getMostSelectedPlayersByNBATeamName(team:string): Observable<Player[]> {
    const url = `${environment.apiHost}selections/team?team=${team}`;
    return this.http.get<Player[]>(url);
  }

  getMostDroppedPlayersByThreshold(threshold:number): Observable<Player[]> {
    const url = `${environment.apiHost}dropped/threshold?threshold=${threshold.toString()}`;
    return this.http.get<Player[]>(url);
  }

  getMostDroppedPlayersByNBATeamName(team:string): Observable<Player[]> {
    const url = `${environment.apiHost}dropped/team?team=${team}`;
    return this.http.get<Player[]>(url);
  }

  getTeamsWithSelections(): Observable<TeamSelection[]> {
    return this.http.get<TeamSelection[]>(environment.apiHost + 'selections/teams-with-selected-players');
  }
}
