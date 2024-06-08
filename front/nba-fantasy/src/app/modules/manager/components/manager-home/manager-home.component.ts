import { Component } from '@angular/core';
import { PlayerDetails, PlayerShortInfo, PlayerStatus } from '../../model/player';
import { PlayerService } from '../../services/player.service';
import { MatSnackBar } from '@angular/material/snack-bar';

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
  teamPlayers: PlayerShortInfo[] = [];

recommendationList: PlayerShortInfo[] = [];
preferencesList: PlayerShortInfo[] = [];
  
  selectedPlayer:PlayerDetails|null=null;

  constructor(private playerService: PlayerService, private _snackBar: MatSnackBar){}

  ngOnInit() {
    this.playerService.getPlayers().subscribe((players) => {
      this.teamPlayers=players;
    });
    this.playerService.getRecommendation().subscribe((lists)=>{
      this.recommendationList=lists.playersRecommendation;
      this.preferencesList=lists.playersPreferences;
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
  removePlayer(player: PlayerShortInfo, event: Event) {
    event.stopPropagation();
    this.playerService.removePlayer(player.id).subscribe((_)=>
    {
      console.log("removed");
      let index = this.teamPlayers.findIndex(player1 => player.id === player1.id);
      this.teamPlayers.splice(index, 1);
      this.recommendationList.push(player);
    });
  }
  addPlayer(player: PlayerShortInfo, event: Event) {
    event.stopPropagation();
    if(this.teamPlayers.length>=10)
      this._snackBar.open("You must drop one player", undefined, {
        duration: 3000
      });
    else{
      this.playerService.addPlayer(player.id).subscribe((player)=>
        {
          this.selectedPlayer=player;
          let player1={fantasyPoints: player.fantasyPoints,id: player.id, name: player.name, nationality: player.nationality, nbaTeam: player.nbaTeam, position:player.position[0], status: player.status};
          this.teamPlayers.push(player1)
          let index = this.recommendationList.findIndex(player => player.id === player1.id);

          this.recommendationList.splice(index, 1);

          index = this.preferencesList.findIndex(player => player.id === player1.id);

          this.preferencesList.splice(index, 1);
        });
    }
  }
  convertToDate(timestamp:number)
  {
    let date = new Date(timestamp);
    return date.getDate()+"/"+(date.getMonth()+1)+"/"+date.getFullYear();
  }
}
