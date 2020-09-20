# (#12 SRF CORONA SCARE APPLICATION Challenge) Corona Scare Map
## Our Approach
To present the corona information in an intuitive and simple way, we use circles with different size and colors (depending on the amount of articles),
whereat the information are load dynamically on moving across the map.
![](https://upload.wikimedia.org/wikipedia/commons/thumb/1/13/Leaflet_logo.svg/1200px-Leaflet_logo.svg.png)
## Used API / Services
The followin api / services where crucial for analyzing the corona news data and display them properly
- *[LeafLet](https://leafletjs.com/)*: Animates the amount of corona cases per region
- *[MapBox API](https://docs.mapbox.com/api/search/)*: Generates the corona map and allows to lookup location (in this case cities)
- *[IBM Cloud Service (NLP Model) API]*(https://www.ibm.com/cloud/watson-natural-language-understanding) Extracts the location out of the news message

## Technology Stack
- *[Angular](https://angular.io/)* Main frontend library ( used to build the webpage => SPA)
- *[Spring Boot](https://spring.io/projects/spring-boot)* Main backend library ( used to process the corona data and share them with the frontend via REST-Endpoints)
