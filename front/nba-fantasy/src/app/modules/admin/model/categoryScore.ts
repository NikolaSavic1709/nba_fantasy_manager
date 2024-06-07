export interface CategoryScores {
  id: number;
  pointScore: number;
  reboundScore: number;
  assistScore: number;
  stealScore: number;
  turnoverScore: number;
  blockScore: number;
  bonusMargin: number;
  isActive: boolean;
}

export interface AddCategoryScores {
  pointScore: number;
  reboundScore: number;
  assistScore: number;
  stealScore: number;
  turnoverScore: number;
  blockScore: number;
  bonusMargin: number;
}