import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import {PlayerService} from '../../services/player.service'
import { Player } from '../../model/player';


const POSITION_MAP: { [key: number]: string } = {
  1: 'PG',
  2: 'SG',
  3: 'SF',
  4: 'PF',
  5: 'C'
};


@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.scss']
})
export class AdminHomeComponent implements AfterViewInit{
  displayedColumns: string[] = ['name', 'position', 'nbaTeam', 'price', 'totalFantasyPoints'];
  dataSource: MatTableDataSource<Player>;
  players: Player[];
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private playerService:PlayerService) {
    this.players = [];
    this.dataSource= new MatTableDataSource();
  }

  ngAfterViewInit() {
    this.playerService.getRecommendationList().subscribe({
      next: (response) => {
        this.players = response;
        console.log(response);
        this.dataSource = new MatTableDataSource(this.players);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (err) => {
        console.log(err);
      }
    });

  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  getPositionLabel(position: number[]): string {
    return position.map(pos => POSITION_MAP[pos]).join(', ');
  }
}

