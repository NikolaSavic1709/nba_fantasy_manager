import { Component, OnInit } from '@angular/core';
import { Injury, NewInjury } from '../../model/injury';
import { InjuryService } from '../../services/injury.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { PlayerBasicInfo } from '../../model/player';
import { Observable, map, startWith } from 'rxjs';

@Component({
  selector: 'app-injuries',
  templateUrl: './injuries.component.html',
  styleUrls: ['./injuries.component.scss']
})
export class InjuriesComponent implements OnInit{
  injuries: Injury[]=[
  ]
  injuryForm = new FormGroup({
    bodyPart: new FormControl(''),
    injuryLevel: new FormControl(''),
    specificBodyPart: new FormControl(''),
    description: new FormControl('', [ Validators.required]),
    playerName: new FormControl<string | PlayerBasicInfo>('',[ Validators.required])
  });
  
  options: PlayerBasicInfo[] = [{id:1,name: 'Mary'}, {id:1,name: 'Shelley'}, {id:1,name: 'Igor'}, {id:1,name: 'Shelley'}, {id:1,name: 'Igor'}, {id:1,name: 'Shelley'}, {id:1,name: 'Igor'}, {id:1,name: 'Shelley'}, {id:1,name: 'Igor'}, {id:1,name: 'Shelley'}, {id:1,name: 'Igor'}];
  filteredOptions: Observable<PlayerBasicInfo[]>|undefined=undefined;

  constructor(private injuryService: InjuryService, private fb: FormBuilder){}

  ngOnInit() {
    this.injuryService.getCurrentInjuries().subscribe((result) => {
      this.injuries=result;
    });
    this.injuryService.getPlayersForAutocomplete().subscribe((result) => {
      this.options=result;
      this.filteredOptions = this.injuryForm.get('playerName')?.valueChanges.pipe(
        startWith(''),
        map(value => {
          const name = typeof value === 'string' ? value : value?.name;
          return name ? this._filter(name as string) : this.options.slice();
        }),
      );
    });
    
  }
  setRecovered(injury: Injury){
    this.injuryService.setRecovery(injury.id).subscribe((_) => {
      console.log("recovered");
      let index = this.injuries.indexOf(injury);
      this.injuries.splice(index, 1);
    })
  }
  calcElapsedTime(timestamp: number){
    // let [day, month, year] = timestamp.split('/').map(Number);
    let date = new Date(timestamp);
    
    let currentDate = new Date();
    
    let timeDiff = currentDate.getTime() - date.getTime();
    
    let daysDiff = Math.floor(timeDiff / (1000 * 60 * 60 * 24));
    
    return daysDiff;
  }
  convertToDate(timestamp:number)
  {
    let date = new Date(timestamp);
    return date.getDate()+"/"+(date.getMonth()+1)+"/"+date.getFullYear();
  }

  addInjury(): void {
    if (this.injuryForm.valid) {
      let formValue=this.injuryForm.value;
      // console.log(formValue)
      let injuryName: string[]=[formValue.bodyPart||'', formValue.injuryLevel||'', formValue.specificBodyPart||'']
      let newInjury: NewInjury={playerId: this.getPlayerId(formValue.playerName!), description: formValue.description!, name: injuryName }
      this.injuryService.addInjury(newInjury).subscribe((result) => {
        this.injuries.push(result)
      })
    }
  }
  getPlayerId(playerName: string | PlayerBasicInfo): number{
    if (this.isPlayerBasicInfo(playerName))
      return playerName.id;
    return -1;

  }
  isPlayerBasicInfo(value: any): value is PlayerBasicInfo {
    return (value && typeof value === 'object' && 'id' in value && 'name' in value);
  }
  displayFn(player: PlayerBasicInfo): string {
    return player && player.name ? player.name : '';
  }

  private _filter(name: string): PlayerBasicInfo[] {
    const filterValue = name.toLowerCase();

    return this.options.filter(option => option.name.toLowerCase().includes(filterValue));
  }
}
