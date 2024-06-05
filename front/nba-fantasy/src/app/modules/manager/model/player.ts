export interface PlayerShortInfo{
    id: number;
    name: string;
    fantasyPoints: number;
    nationality: string
    nbaTeam: string
    position: number
    status: PlayerStatus
    }

export interface PlayerDetails{
    id: number;
    name: string;
    totalFantasyPoints: number;
    totalBonusPoints: number;
    nationality: string
    nbaTeam: string
    position: number[]
    birthday: string
    price: number
    status: PlayerStatus,
    ppg: number,
    rpg: number,
    apg: number,
    gp: number,
    mpg: number,
    spg: number,
    tpg: number,
    bpg: number,
    pfpg: number,
    fgPercentage: number,
    twoPointPercentage: number,
    threePointPercentage: number
}
export enum PlayerStatus {
    HEALTHY, OUT
    }

