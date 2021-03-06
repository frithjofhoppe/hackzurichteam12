import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {ScareEvaluation, ScareMapSearch} from "./api.model";

@Injectable({
  providedIn: 'root'
})
export class ScareMapService {

  readonly baseUrl = '/api/scareEvaluations';

  constructor(
    private http: HttpClient
  ) {
  }

  getScareEvaluations(): Observable<ScareEvaluation[]> {
    return this.http.get<ScareEvaluation[]>(this.baseUrl);
  }

  searchInScareMap(searchDto: ScareMapSearch): Observable<ScareEvaluation[]> {
    return this.http.post<ScareEvaluation[]>(this.baseUrl, searchDto);
  }
}
