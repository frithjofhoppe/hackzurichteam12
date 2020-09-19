package com.hackzurich.hackzurichteam12.backend.service;

import com.hackzurich.hackzurichteam12.backend.api.LocationRecognitionResult;
import com.hackzurich.hackzurichteam12.backend.api.LocationRecognizerService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class DefaultLocationRecognizerService implements LocationRecognizerService {

    private static final String IBM_NPL_NRM_API = "https://api.eu-de.natural-language-understanding.watson.cloud.ibm.com/instances/71edc145-e0e7-443d-8f79-62322b0ddd37";
    private static final String ACCESS_KEY = "bZVfLE_1CbmP7ssWjS5wAldEagNCnP02P9TtELjKQdew";
    private final WebClient web;

    public DefaultLocationRecognizerService(WebClient web) {
        this.web = web;
    }


    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(IBM_NPL_NRM_API)
                .build();
    }


    @Override
    public LocationRecognitionResult findLocationInMessage(String message) {
        var response = web
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
                                        message
                                )), String.class
                        )
                ).retrieve()
                .bodyToMono();
    }

}
