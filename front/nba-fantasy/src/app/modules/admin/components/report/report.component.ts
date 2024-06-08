import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ReportService } from '../../services/report.service';
import { MatTableDataSource } from '@angular/material/table';
import { Player } from '../../model/player';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { HttpErrorResponse } from '@angular/common/http';
import { TeamSelection } from '../../model/teamSelection';
import { InjuryStats } from '../../model/injury';

const POSITION_MAP: { [key: number]: string } = {
  1: 'PG',
  2: 'SG',
  3: 'SF',
  4: 'PF',
  5: 'C'
};

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements AfterViewInit{

  hasError = false;

  displayedColumns: string[] = ['name', 'position', 'nbaTeam', 'timesSelected', 'timesDropped', 'fantasyPoints'];
  dataSource: MatTableDataSource<Player>;
  players: Player[];
  @ViewChild('paginator1') paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  displayedColumns2: string[] = ['team', 'timesSelected'];
  dataSource2: MatTableDataSource<TeamSelection>;
  teamsSelection: TeamSelection[];
  @ViewChild('paginator2') paginator2!: MatPaginator;
  @ViewChild(MatSort) sort2!: MatSort;

  displayedColumns3: string[] = ['injuryName', 'occurrence', 'averageRecoveryTime'];
  dataSource3: MatTableDataSource<InjuryStats>;
  injuryStats: InjuryStats[];
  @ViewChild('paginator3') paginator3!: MatPaginator;
  @ViewChild(MatSort) sort3!: MatSort;


  constructor(
    private reportService: ReportService
  ) {
    this.players = [];
    this.dataSource = new MatTableDataSource();

    this.teamsSelection = [];
    this.dataSource2 = new MatTableDataSource();

    this.injuryStats = [];
    this.dataSource3 = new MatTableDataSource();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.dataSource2.paginator = this.paginator2;
    this.dataSource2.sort = this.sort2;
    this.dataSource3.paginator = this.paginator3;
    this.dataSource3.sort = this.sort3;
  }

  // REPORT 1

  selectThresholdForm = new FormGroup({
    threshold1: new FormControl('', [Validators.min(0), Validators.max(100), Validators.required]),
  });

  report1Error = false;


  selectThreshold() {
    const threshold = Number(this.selectThresholdForm.value.threshold1);

    if (this.selectThresholdForm.valid) {
      this.reportService.getMostSelectedPlayersByThreshold(threshold).subscribe({
        next: (response) => {
          this.players = response;
          console.log(response);
          this.dataSource = new MatTableDataSource(this.players);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        },
        error: (error) => {
          if (error instanceof HttpErrorResponse) {
            this.report1Error = true;
          }
        },
      });
    }
  }


  // REPORT 2

  nbaTeams: string[] = [
    'Atlanta Hawks',
    'Boston Celtics',
    'Brooklyn Nets',
    'Charlotte Hornets',
    'Chicago Bulls',
    'Cleveland Cavaliers',
    'Dallas Mavericks',
    'Denver Nuggets',
    'Detroit Pistons',
    'Golden State Warriors',
    'Houston Rockets',
    'Indiana Pacers',
    'Los Angeles Clippers',
    'Los Angeles Lakers',
    'Memphis Grizzlies',
    'Miami Heat',
    'Milwaukee Bucks',
    'Minnesota Timberwolves',
    'New Orleans Pelicans',
    'New York Knicks',
    'Oklahoma City Thunder',
    'Orlando Magic',
    'Philadelphia 76ers',
    'Phoenix Suns',
    'Portland Trail Blazers',
    'Sacramento Kings',
    'San Antonio Spurs',
    'Toronto Raptors',
    'Utah Jazz',
    'Washington Wizards'
  ];

  selectTeamForm = new FormGroup({
    team1: new FormControl('', [Validators.required]),
  });

  report2Error = false;


  selectTeam() {
    const team = this.selectTeamForm.value.team1!;

    if (this.selectTeamForm.valid) {
      this.reportService.getMostSelectedPlayersByNBATeamName(team).subscribe({
        next: (response) => {
          this.players = response;
          console.log(response);
          this.dataSource = new MatTableDataSource(this.players);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        },
        error: (error) => {
          if (error instanceof HttpErrorResponse) {
            this.report2Error = true;
          }
        },
      });
    }
  }

  // REPORT 3

  droppedThresholdForm = new FormGroup({
    threshold2: new FormControl('', [Validators.min(0), Validators.max(100), Validators.required]),
  });

  report3Error = false;


  droppedThreshold() {
    const threshold = Number(this.droppedThresholdForm.value.threshold2);

    if (this.droppedThresholdForm.valid) {
      this.reportService.getMostDroppedPlayersByThreshold(threshold).subscribe({
        next: (response) => {
          this.players = response;
          console.log(response);
          this.dataSource = new MatTableDataSource(this.players);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        },
        error: (error) => {
          if (error instanceof HttpErrorResponse) {
            this.report3Error = true;
          }
        },
      });
    }
  }


  // REPORT 4

  droppedTeamForm = new FormGroup({
    team2: new FormControl('', [Validators.required]),
  });

  report4Error = false;


  droppedTeam() {
    const team = this.droppedTeamForm.value.team2!;

    if (this.droppedTeamForm.valid) {
      this.reportService.getMostDroppedPlayersByNBATeamName(team).subscribe({
        next: (response) => {
          this.players = response;
          console.log(response);
          this.dataSource = new MatTableDataSource(this.players);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        },
        error: (error) => {
          if (error instanceof HttpErrorResponse) {
            this.report4Error = true;
          }
        },
      });
    }
  }


  // REPORT 5

  report5Error = false;

  getTeamSelections() {

    this.reportService.getTeamsWithSelections().subscribe({
      next: (response) => {
        this.teamsSelection = response;
        console.log(response);
        this.dataSource2 = new MatTableDataSource(this.teamsSelection);
        this.dataSource2.paginator = this.paginator2;
        this.dataSource2.sort = this.sort2;
      },
      error: (error) => {
        if (error instanceof HttpErrorResponse) {
          this.report5Error = true;
        }
      },
    });

  }

    // REPORT 6

    injuryThresholdForm = new FormGroup({
      threshold3: new FormControl('', [Validators.min(0), Validators.max(100), Validators.required]),
    });
  
    report6Error = false;
  
  
    injuryThreshold() {
      const threshold = Number(this.injuryThresholdForm.value.threshold3);
  
      if (this.injuryThresholdForm.valid) {
        this.reportService.getMostFrequentInjuriesByThreshold(threshold).subscribe({
          next: (response) => {
            this.injuryStats = response;
            console.log(response);
            this.dataSource3 = new MatTableDataSource(this.injuryStats);
            this.dataSource3.paginator = this.paginator3;
            this.dataSource3.sort = this.sort3;
          },
          error: (error) => {
            if (error instanceof HttpErrorResponse) {
              this.report6Error = true;
            }
          },
        });
      }
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
