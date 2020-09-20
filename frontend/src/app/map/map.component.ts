import {Component, OnInit} from '@angular/core';
import * as leaflet from 'leaflet';
import {ScareMapService} from "../api/scare-map.service";
import {scan, tap} from "rxjs/operators";
import {Coordinates, ScareMapSearch, ScareEvaluation, ScareLevel} from "../api/api.model";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

  // todo need to be updated when scrolling
  coordinates: Coordinates = {
    longitude: 46.942969,
    latitude: 7.432527
  };
  leafleatMap: any;
  readonly leafleatMapId = 'map';
  readonly defaultZoom = 13;
  currentZoom = this.defaultZoom;
  addElements: any[] = [];
  panelOpenState: boolean;

  constructor(private scareMapService: ScareMapService) {
  }

  ngOnInit(): void {

    // Select current location
    this.leafleatMap = leaflet
      .map(this.leafleatMapId)
      .setView(this.coordinateToPosition(this.coordinates), this.defaultZoom);

    this.startZoomEventListener();
    this.startMoveEventListener();
    this.insertNewMarkers();

    // Add marker to current position
    leaflet
      .marker(this.coordinateToPosition(this.coordinates))
      .addTo(this.leafleatMap);

    this.initializeMap();

  }

  coordinateToPosition(cooridnate: Coordinates): [number, number] {
    return [cooridnate.longitude, cooridnate.latitude];
  }

  private initializeMap() {
    // todo token should be generated automatically
    leaflet.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoiZnJpdGhqb2Zob3BwZSIsImEiOiJja2Y5NzlpbmkwamV0MnJxczlvNWFxMWdzIn0.7NNcGk2Uq0QkBTBlfvQXvA', {
      maxZoom: 18,
      attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
        '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
        'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
      id: 'mapbox/streets-v11',
      tileSize: 512,
      zoomOffset: -1
    }).addTo(this.leafleatMap);
  }

  private removeAllElementsFromMap() {
    this.addElements.forEach(element => element.remove());
  }

  private startZoomEventListener() {
    this.leafleatMap.on('zoomanim', event => {
      this.currentZoom = event.zoom
      this.removeAllElementsFromMap();
      this.insertNewMarkers();
    });
  }

  private startMoveEventListener() {
    this.leafleatMap.on('moveend', e => {
      var bounds = this.leafleatMap.getBounds()
      let searchMap = {
        northEast: {
          longitude: bounds._northEast.lng,
          latitude: bounds._northEast.lat
        } as Coordinates,
        southWest: {
          latitude: bounds._southWest.lat,
          longitude: bounds._southWest.lng
        } as Coordinates
      } as ScareMapSearch
    });
  }

  private insertNewMarkers() {
    this.scareMapService
      .getScareEvaluations()
      .subscribe(evaluations => evaluations.forEach(evaluation => {
        console.log(evaluation);
        console.log(evaluation.coordinates.longitude);
        this.addElements.push(this.generateMarker(evaluation));
      }));
  }

  private generateMarker(evaluation: ScareEvaluation) {
    const circle = leaflet.circle(this.coordinateToPosition(evaluation.coordinates), {
      color: this.getScareLevel(evaluation).color,
      fillColor: this.getScareLevel(evaluation).color,
      fillOpacity: 0.5,
      radius: this.getScareLevel(evaluation).radius
    }).addTo(this.leafleatMap);
    circle.bindPopup(`${evaluation.numberOfArticles} articles`);
    return circle;
  }

  private getScareLevel(evaluation: ScareEvaluation): ScareLevel {
    let radius;
    let color;
    if (this.currentZoom <= 8) {
      radius = 3000;
    } else if (this.currentZoom <= 10) {
      radius = 2000;
    } else if (this.currentZoom <= 11) {
      radius = 1000;
    } else if (this.currentZoom <= 13) {
      radius = 500;
    } else if (this.currentZoom <= 15) {
      radius = 300;
    }

    if (evaluation.numberOfArticles >= 100) {
      color = 'red'
    } else if (evaluation.numberOfArticles >= 20) {
      color = 'orange'
    } else {
      color = 'green';
    }

    return {
      color: color,
      radius: radius * (evaluation.numberOfArticles / 60)
    } as ScareLevel;
  }
}
