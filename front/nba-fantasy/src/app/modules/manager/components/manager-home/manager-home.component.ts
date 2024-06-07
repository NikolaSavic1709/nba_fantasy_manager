import { Component } from '@angular/core';
import { PlayerDetails, PlayerShortInfo, PlayerStatus } from '../../model/player';
import { PlayerService } from '../../services/player.service';

@Component({
  selector: 'app-manager-home',
  templateUrl: './manager-home.component.html',
  styleUrls: ['./manager-home.component.scss']
})
export class ManagerHomeComponent {
  teamName="Partizan"
  positionMap:  { [key: number]: string } = {
    1: "PG",
    2: "SG",
    3: "SF",
    4: "PF",
    5: "C"
  };
  teamPlayers: PlayerShortInfo[] = [
    {id:1, position:2, name: 'Bogdan Bogdanovic', nationality: "Serbia", nbaTeam: "Atlanta Hawks", fantasyPoints: 100, status: 'OUT' },
    {id:1, position:1, name: 'Dante Exum', nationality: "Australia", nbaTeam: "Dallas Mavericks", fantasyPoints: 100, status: 'HEALTHY' },
    {id:1, position:3, name: 'Davis Bertans', nationality: "Lativa", nbaTeam: "Charlotte Hornets", fantasyPoints: 100, status: 'HEALTHY' },
    {id:1, position:1, name: 'Vasilije Micic', nationality: "Serbia", nbaTeam: "Charlotte Hornets", fantasyPoints: 100, status: 'OUT' },
    {id:1, position:4, name: 'Aleksej Pokusevski', nationality: "Serbia", nbaTeam: "Charlotte Hornets", fantasyPoints: 100, status: 'HEALTHY' },
    {id:1, position:5, name: 'Nikola Jokic', nationality: "Serbia", nbaTeam: "Denver Nuggets", fantasyPoints: 100, status: 'HEALTHY' },
    {id:1, position:3, name: 'Nikola Jovic', nationality: "Serbia", nbaTeam: "Miami Heat", fantasyPoints: 100, status: 'HEALTHY' }
];

recommendationList: PlayerShortInfo[] = [
  {id:1, position:2, name: 'Bogdan Bogdanovic', nationality: "Serbia", nbaTeam: "Atlanta Hawks", fantasyPoints: 100, status: 'OUT' },
  {id:1, position:1, name: 'Dante Exum', nationality: "Australia", nbaTeam: "Dallas Mavericks", fantasyPoints: 100, status: 'HEALTHY'},
  {id:1, position:3, name: 'Davis Bertans', nationality: "Lativa", nbaTeam: "Charlotte Hornets", fantasyPoints: 100, status: 'HEALTHY' },
  {id:1, position:1, name: 'Vasilije Micic', nationality: "Serbia", nbaTeam: "Charlotte Hornets", fantasyPoints: 100, status: 'OUT' },
  {id:1, position:4, name: 'Aleksej Pokusevski', nationality: "Serbia", nbaTeam: "Charlotte Hornets", fantasyPoints: 100, status: 'HEALTHY' },
  {id:1, position:5, name: 'Nikola Jokic', nationality: "Serbia", nbaTeam: "Denver Nuggets", fantasyPoints: 100, status: 'HEALTHY' },
  {id:1, position:3, name: 'Nikola Jovic', nationality: "Serbia", nbaTeam: "Miami Heat", fantasyPoints: 100, status: 'HEALTHY' }
];
  // playerDetails: PlayerDetails={
  //   id:1, name: "Bogdan Bogdanovic", bonusPoints: 10, fantasyPoints: 10, nationality:'Serbia',
  //   nbaTeam: 'Atlanta Hawks', position:[2,3], birthday: "11/11/1992", price: 80, status: 'OUT,
  //   ppg: 20.0, rpg: 3.3, apg: 3.4, gp: 70, mpg: 35.1, spg: 2.1, tpg: 1.5, bpg: 0.5,
  //   pfpg: 2.3, fgPercentage: 45.5, twoPointPercentage: 48.6, threePointPercentage: 42.4
  // }
  selectedPlayer:PlayerDetails|null=null;

  constructor(private playerService: PlayerService){}

  ngOnInit() {
    this.playerService.getPlayers().subscribe((players) => {
      this.teamPlayers=players;
    });
  }

  

  transformPos(position: number[]): string{
    return position.map(pos => this.positionMap[pos]).join(', ');
  }
  openPlayer(player: PlayerShortInfo) {
    this.playerService.getPlayerDetails(player.id).subscribe((playerDetails)=>{
      this.selectedPlayer=playerDetails;
    });
    
  }
  removePlayer(player: PlayerShortInfo) {
    //pozvati servis sa tim id-em
  }
  addPlayer(player: PlayerShortInfo){

  }
}
