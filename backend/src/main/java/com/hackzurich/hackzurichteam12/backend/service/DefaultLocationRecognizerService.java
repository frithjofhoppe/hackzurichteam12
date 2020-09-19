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

    private static final String IBM_NPL_NRM_API = "https://api.eu-de.natural-language-understanding.watson.cloud.ibm.com/instances/71edc145-e0e7-443d-8f79-62322b0ddd37";
    private static final String ACCESS_KEY = "bZVfLE_1CbmP7ssWjS5wAldEagNCnP02P9TtELjKQdew";

    private static final String MAPBOX_API = "https://api.mapbox.com/geocoding/v5/mapbox.places";
    private static final String MAPBOX_KEY = "pk.eyJ1IjoiZnJpdGhqb2Zob3BwZSIsImEiOiJja2Y5NzhiOWMwYnhqMndvNWo0Mjd6dmxiIn0.nusdy9-fGkx4Al2H-ct4GQ";


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
    public LocationRecognitionResult findCityCoordinates(String news) {
        var locations = findLocationInNews(news);

        for (IBMTextEntity textEntity : locations) {
            var city = lookupLocation(textEntity.getText());
            if (Objects.nonNull(city)) {
                return city;
            }
        }
        return null;
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
