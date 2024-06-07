export interface Player {
    id: string;
    name: string;
    nationality: string;
    position: number[];
    birthDate: string; 
    fantasyPoints:number;
    bonusPoints:number;
    price:number;
    status: PlayerStatus;
    nbaTeam: string;
    statisticalColumns:StatisticalColumns;
    fantasyStatisticalColumns: FantasyStatisticalColumns;
  }
  
  export enum PlayerStatus{
    "HEALTHY", "OUT", null
  }
  
  export interface StatisticalColumns{
    ppg: number;
    rpg: number;
    apg: number;
    gp: number;
    mpg: number;
    spg: number;
    tpg: number;
    bpg: number;
    pfpg: number;
    fgPercentage: number;
    twoPointPercentage: number;
    threePointPercentage: number;
  }
  
  export interface FantasyStatisticalColumns{
    timesSelected: number;
    timesDropped: number;
    recommendationRank: number;
  }

  export interface PlayerBasicInfo{
    id: number,
    name: string
  }