import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AddFilter, Filter } from '../model/filter';
import { environment } from 'src/environments/environment';
import { Player } from '../model/player';

@Injectable({
  providedIn: 'root'
})
export class FilterService {

  private headers = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient) {
  }

  getFilters(): Observable<Filter[]> {
    return this.http.get<Filter[]>(environment.apiHost + 'filter/all');
  }

  filterPlayers(id:number): Observable<Player[]> {
    const url = `${environment.apiHost}filter?startFilter=${id.toString()}`;
    return this.http.get<Player[]>(url);
  }

  addFilter(filter:AddFilter): Observable<Filter> {
    return this.http.post<Filter>(environment.apiHost + "filter",
      {
        minPrice: filter.minPrice,
        maxPrice: filter.maxPrice,
        team: filter.team,
        position: filter.position
      }, {"headers": this.headers})
  }
}
