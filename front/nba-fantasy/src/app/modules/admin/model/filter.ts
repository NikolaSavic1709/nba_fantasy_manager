export interface Filter {
    id: number;
    minPrice: number;
    maxPrice: number;
    team: string | null | undefined;
    position: number;
  }
  
  export interface AddFilter {
    minPrice: number;
    maxPrice: number;
    team: string | null | undefined;
    position: number;
  }