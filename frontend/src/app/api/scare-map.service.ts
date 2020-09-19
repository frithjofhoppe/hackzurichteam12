import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {ScareEvaluation} from "./api.model";

@Injectable({
  providedIn: 'root'
})
export class ScareMapService {

  constructor(
    private http: HttpClient
  ) {

  }

  getScareEvaluations(): Observable<ScareEvaluation[]> {
    return of([
      {
        coordinates: {
          lat:  7.444381,
          long:46.948848,
        },
        areaName: 'Bern',
        numberOfArticles: 120
      } as ScareEvaluation,

      {
        coordinates: {
          lat: 7.452899,
          long: 46.997327
        },
        areaName: 'Zollikofen',
        numberOfArticles: 60
      } as ScareEvaluation
    ])
  }


}
