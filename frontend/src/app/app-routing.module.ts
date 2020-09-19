import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {MapComponent} from "./map/map.component";

const routes: Routes = [
  {component: MapComponent, path: 'map'},
  {path: '**', redirectTo: '/map'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
