package com.hackzurich.hackzurichteam12.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackzurich.hackzurichteam12.backend.api.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DefaultLocationRecognizerService implements LocationRecognizerService {

    //frithjof.hoppe@gmail.com
    private static final String IBM_NPL_NRM_API = "https://api.eu-de.natural-language-understanding.watson.cloud.ibm.com/instances/71edc145-e0e7-443d-8f79-62322b0ddd37";
    private static final String ACCESS_KEY = "bZVfLE_1CbmP7ssWjS5wAldEagNCnP02P9TtELjKQdew";

    // frithjof.hoppe@hotmail.ch
    private static final String IBM_NPL_NRM_API_2 = "https://api.eu-gb.natural-language-understanding.watson.cloud.ibm.com/instances/93b50386-2855-47e5-9178-ee8bf15cc036";
    private static final String ACCESS_KEY_2 = "YHVQL6m8iwgzUH-vd6-9d8EUNHIHfyOFaBpnVdRa3poa";


    private static final String MAPBOX_API = "https://api.mapbox.com/geocoding/v5/mapbox.places";
    // frithjof.hoppe@gmail.com
    private static final String MAPBOX_KEY = "pk.eyJ1IjoiZnJpdGhqb2Zob3BwZSIsImEiOiJja2Y5NzhiOWMwYnhqMndvNWo0Mjd6dmxiIn0.nusdy9-fGkx4Al2H-ct4GQ";
    // frithjof.hoppe@hotmail.ch
    private static final String MAPBOX_KEY_2 = "pk.eyJ1IjoiZnJpdGhqb2Zob3BwZWhvdG1haWwiLCJhIjoiY2tmYTY2bXQzMHN2ODJzbGMzaXlkYzg4biJ9.z6VOVgsUIzyvu-X1WhzyPQ";


    public DefaultLocationRecognizerService() {

    }

    public WebClient webClientGeocode() {
        return WebClient.builder()
                .baseUrl(MAPBOX_API)
                .build();
    }

    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(IBM_NPL_NRM_API)
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(logRequest());
                    exchangeFilterFunctions.add(logResponse());
                })
                .defaultHeaders(header -> header.setBasicAuth("apikey", ACCESS_KEY))
                .build();
    }

    ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            StringBuilder sb = new StringBuilder("Request: \n");
            //append clientRequest method and url
            System.out.println(clientRequest.url());
            try {
                System.out.println(
                        (new ObjectMapper()).writeValueAsString(clientRequest)
                );
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            System.out.println(clientRequest.body().toString());
            clientRequest
                    .headers()
                    .forEach((name, values) -> values.forEach(value -> System.out.println(value)));
            return Mono.just(clientRequest);
        });
    }

    ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientRequest -> {
            StringBuilder sb = new StringBuilder("Request: \n");
            //append clientRequest method and url
            System.out.println(clientRequest);
            return Mono.just(clientRequest);
        });
    }

    @Override
    public List<IBMTextEntity> findLocationInNews(String news) {
        return analyzeRawNews(news)
                .getEntities()
                .stream()
                .filter(result -> result.getType().contains("Location"))
                .collect(Collectors.toList());
    }

    @Override
    public LocationRecognitionResult lookupLocation(String location) {
        var result = webClientGeocode()
                .method(HttpMethod.GET)
                .uri(
                        "/" + location + "%20Switzerland.json?access_token=" + MAPBOX_KEY
                ).retrieve()
                .bodyToMono(MapboxPlacesResult.class)
                .block();

        if (result.getFeatures().length > 0) {

            var bestMatchingFeature = result.getFeatures()[0];

            if (bestMatchingFeature.getPlace_name().contains("Switzerland")) {

                var coordinates = bestMatchingFeature.getGeometry().getCoordinates();
                return LocationRecognitionResult.builder()
                        .city(bestMatchingFeature.getText())
                        .coordinates(
                                CoordinatesDto.builder()
                                        .longitude(coordinates[1])
                                        .latitude(coordinates[0])
                                        .build()
                        ).build();
            }
        }

        return null;
    }

    @Override
    public LocationRecognitionResult findCityCoordinatesByMessage(String news) {
        var locations = findLocationInNews(news);

        for (IBMTextEntity textEntity : locations) {
            var city = lookupLocation(textEntity.getText());
            if (Objects.nonNull(city)) {
                return city;
            }
        }
        return null;
    }

    @Override
    public LocationRecognitionResult findCityByCoordinates(double latitude, double longitude) {
        var placeResult = webClientGeocode()
                .method(HttpMethod.GET)
                .uri(
                        "/" + longitude + "," + latitude + ".json?access_token=" + MAPBOX_KEY
                ).retrieve()
                .bodyToMono(MapboxPlacesResult.class)
                .block();

        var place = Arrays.asList(placeResult.getFeatures())
                .stream()
                .filter(feature -> feature.getId().contains("place"))
                .filter(feature -> feature.getPlace_name().contains("Switzerland"))
                .findFirst();

        var location = place
                .map(p -> LocationRecognitionResult.builder()
                        .city(p.getText())
                        .coordinates(
                                CoordinatesDto.builder()
                                        .latitude(latitude)
                                        .longitude(longitude)
                                        .build()
                        ).build()
                )
                .orElse(null);
        return location;
    }

    private IBMTextAnalyzeResult analyzeRawNews(String news) {

        return webClient()
                .method(HttpMethod.POST)
                .uri("/v1/analyze?version=2020-08-01")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.just(
                                IBMTextAnalyzeRequest.builder()
                                        .text(news)
                                        .features(
                                                IBMTextFeature.builder()
                                                        .entities(
                                                                IBMTextEntityRequest
                                                                        .builder()
                                                                        .sentiment(true)
                                                                        .build()
                                                        ).build()
                                        ).build()
                        ), IBMTextAnalyzeRequest.class
                ).retrieve()
                .bodyToMono(IBMTextAnalyzeResult.class)
                .block();
    }

}
