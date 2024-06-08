import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import {PlayerService} from '../../services/player.service'
import { Player } from '../../../admin/model/player';
import { FilterService } from '../../../admin/services/filter.service';
import { MatDialog } from '@angular/material/dialog';
import { AddFilterDialogComponent } from '../../../admin/components/add-filter-dialog/add-filter-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Filter } from '../../../admin/model/filter';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';


const POSITION_MAP: { [key: number]: string } = {
  1: 'PG',
  2: 'SG',
  3: 'SF',
  4: 'PF',
  5: 'C'
};


@Component({
  selector: 'app-recommendation',
  templateUrl: './recommendation.component.html',
  styleUrls: ['./recommendation.component.scss']
})
export class RecommendationComponent implements AfterViewInit{
  displayedColumns: string[] = ['name', 'position', 'nbaTeam', 'price', 'fantasyPoints'];
  dataSource: MatTableDataSource<Player>;
  players: Player[];
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  filters: Filter[];

  selectedRow: any|null= null;

  selectRow(row: any): void {
    this.selectedRow = row;
    console.log('Selected row:', this.selectedRow);
  }
  addPlayer(){
    let player: Player = this.selectedRow as unknown as Player
    
    this.playerService.addPlayer(Number(player.id)).subscribe((player)=>
      {
        console.log('added');
      });
    
  }

  constructor(private playerService:PlayerService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private filterService:FilterService
  ) {
    this.players = [];
    this.filters = [];
    this.dataSource= new MatTableDataSource();
  }

  setFilterForm = new FormGroup({
    filterControl: new FormControl('', [Validators.required]),
  });


  filterError = false;

  ngAfterViewInit() {
    this.playerService.getRecommendationList().subscribe({
      next: (response) => {
        this.players = response;
        console.log(response);
        this.dataSource = new MatTableDataSource(this.players);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.refreshFilters();
      },
      error: (err) => {
        console.log(err);
      }
    });

  }

  setFilter() {
    const filterId = Number(this.setFilterForm.value.filterControl)

    if (this.setFilterForm.valid) {
      this.filterService.filterPlayers(filterId).subscribe({
        next: (response) => {
          this.players = response;
          console.log(response);
          this.dataSource = new MatTableDataSource(this.players);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        },
        error: (error) => {
          if (error instanceof HttpErrorResponse) {
            this.filterError = true;
          }
        },
      });
    }
  }


  addFilter(){
    const dialogRef = this.dialog.open(AddFilterDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result == "success") {
        this.openSnackBar("Filter added successfully");
        this.refreshFilters();
      }
    });
  }

  refreshFilters(){
    this.filterService.getFilters().subscribe({
      next: (response) => {
        this.filters = response.sort((a, b) => a.id - b.id);
        console.log(response);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  openSnackBar = (message: string) => {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      verticalPosition: 'bottom',
      horizontalPosition: 'center',
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

  getPosition(position: number): string {
    return POSITION_MAP[position];
  }
}

