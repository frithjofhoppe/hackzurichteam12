export interface ScareEvaluation {
  coordinates: Coordinates;
  numberOfArticles: number;
  areaName: string;
  areaPopulation?: number;
}

export interface Coordinates {
  long: number;
  lat: number;
}

export interface MapClick {
  latLng: {
    lat: number,
    lng: number
  }
}

export interface ScareLevel {
  radius: number;
  color: string;
}
