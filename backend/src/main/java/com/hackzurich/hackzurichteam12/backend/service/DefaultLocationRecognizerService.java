package com.hackzurich.hackzurichteam12.backend.service;

import com.hackzurich.hackzurichteam12.backend.api.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class DefaultLocationRecognizerService implements LocationRecognizerService {

    private static final String IBM_NPL_NRM_API = "https://api.eu-de.natural-language-understanding.watson.cloud.ibm.com/instances/71edc145-e0e7-443d-8f79-62322b0ddd37";
    private static final String ACCESS_KEY = "bZVfLE_1CbmP7ssWjS5wAldEagNCnP02P9TtELjKQdew";

    private static final String MAPBOX_API = "https://api.mapbox.com/geocoding/v5/mapbox.places";
    private static final String MAPBOX_KEY = "pk.eyJ1IjoiZnJpdGhqb2Zob3BwZSIsImEiOiJja2Y5NzhiOWMwYnhqMndvNWo0Mjd6dmxiIn0.nusdy9-fGkx4Al2H-ct4GQ";


    public DefaultLocationRecognizerService() {

    }

    public WebClient webClientGeocode() {
        return WebClient.builder()
                .baseUrl(IBM_NPL_NRM_API)
                .build();
    }

    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(IBM_NPL_NRM_API)
                .build();
    }

    @Override
    public IBMTextEntity findLocationInNews(String news) {
        var analyzeResult = analyzeRawNews(news)
                .getEntities()
                .stream()
                .filter(result -> result.getType().contains("Location"))
                .findFirst();

        return null;
    }

    @Override
    public LocationRecognitionResult getCoordinatesOfLocation(String location) {
        var result = webClientGeocode()
                .method(HttpMethod.GET)
                .uri(
                        String.format(
                                "/%s%20Switzerland.json?access_token=%s",
                                location,
                                MAPBOX_KEY
                        )
                ).retrieve()
                .bodyToMono(MapboxPlacesResult.class)
                .block();

        if (result.getFeatures().length > 0) {
            var feature = result.getFeatures()[0];
            var coordinates = feature.getGeometry().getCoordinates();
            return LocationRecognitionResult.builder()
                    .city(feature.getText())
                    .coordinates(
                            CoordinatesDto.builder()
                                    .longitude(coordinates[1])
                                    .latitude(coordinates[0])
                                    .build()
                    ).build();
        }

        return null;
    }

    private IBMTextAnalyzeResult analyzeRawNews(String news) {
        return webClient()
                .method(HttpMethod.POST)
                .uri("/v1/analyze?version=2020-08-01")
                .accept(MediaType.APPLICATION_JSON)
                .body(
                        BodyInserters.fromPublisher(
                                Mono.just(String.format(
                                        "{\n" +
                                                "  \"text\": \"%s\",\n" +
                                                "  \"features\": {\n" +
                                                "    \"entities\": {\n" +
                                                "      \"sentiment\": true,\n" +
                                                "      \"limit\": 1\n" +
                                                "    }\n" +
                                                "  }\n" +
                                                "}",
                                        news
                                )), String.class
                        )
                ).retrieve()
                .bodyToMono(IBMTextAnalyzeResult.class)
                .block();
    }

}
