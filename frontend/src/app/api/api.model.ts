export interface ScareEvaluation {
  coordinates: Coordinates;
  numberOfArticles: number;
  areaName: string;
  areaPopulation?: number;
}

export interface Coordinates {
  longitude: number;
  latitude: number;
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

export interface ScareMapSearch {
  southWest: Coordinates;
  northEast: Coordinates;
  city: string;
}
