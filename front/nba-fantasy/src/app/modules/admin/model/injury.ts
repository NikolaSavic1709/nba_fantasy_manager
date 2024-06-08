export interface Injury {
    id: number;
    player: string,
    team: string,
    description: string;
    isRecovered: boolean;
    recoveryTimeInDays: number | null;
    estimatedRecoveryTimeInDays: number;
    timestamp: number
}

export interface NewInjury {
    playerId: number,
    description: string,
    name: string[]
}

export interface InjuryStats {
    injuryName: string,
    occurrence: number,
    averageRecoveryTime: number
}