import {Component, OnInit} from '@angular/core';
import * as leaflet from 'leaflet';
import {ScareMapService} from "../api/scare-map.service";
import {scan, tap} from "rxjs/operators";
import {Coordinates, ScareEvaluation, ScareLevel} from "../api/api.model";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

  // todo need to be updated when scrolling
  coordinates: Coordinates = {
    long: 46.942969,
    lat: 7.432527
  };
  leafleatMap: any;
  readonly leafleatMapId = 'map';
  readonly defaultZoom = 13;
  currentZoom = this.defaultZoom;
  addElements: any[] = [];

  constructor(private scareMapService: ScareMapService) {
  }

  ngOnInit(): void {

    // Select current location
    this.leafleatMap = leaflet
      .map(this.leafleatMapId)
      .setView(this.coordinateToPosition(this.coordinates), this.defaultZoom);

    this.startZoomEventListener();
    this.insertNewMarkers();

    // Add marker to current position
    leaflet
      .marker(this.coordinateToPosition(this.coordinates))
      .addTo(this.leafleatMap);

    this.initializeMap();

  }

  coordinateToPosition(cooridnate: Coordinates): [number, number] {
    return [cooridnate.long, cooridnate.lat];
  }

  private initializeMap() {
    leaflet.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
      maxZoom: 18,
      attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
        '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
        'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
      id: 'mapbox/streets-v11',
      tileSize: 512,
      zoomOffset: -1
    }).addTo(this.leafleatMap);
  }

  private startZoomEventListener() {
    this.leafleatMap.on('zoomanim', event => {
      this.currentZoom = event.zoom
      this.addElements.forEach(element => element.remove())
      this.insertNewMarkers();
    });
  }

  private insertNewMarkers() {
    this.scareMapService
      .getScareEvaluations()
      .subscribe(evaluations => evaluations.forEach(evaluation => {
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
      radius: radius *  (evaluation.numberOfArticles / 60)
    } as ScareLevel;
  }
}
