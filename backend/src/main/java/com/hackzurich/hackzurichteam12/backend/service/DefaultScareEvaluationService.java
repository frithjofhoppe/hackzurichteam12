package com.hackzurich.hackzurichteam12.backend.service;

import com.hackzurich.hackzurichteam12.backend.api.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DefaultScareEvaluationService implements ScareEvaluationService {

    private final LocationRecognizerService locationRecognizerService;

    public DefaultScareEvaluationService(LocationRecognizerService locationRecognizerService) {
        this.locationRecognizerService = locationRecognizerService;
    }

    @Override
    public List<ScareEvaluationDto> getEvaluations() {
        locationRecognizerService.lookupLocation("Zurich");
        return Stream.of(
                ScareEvaluationDto.builder()
                        .areaName("Bern")
                        .numberOfArticles(120)
                        .areaPopulation(0)
                        .coordinates(
                                CoordinatesDto.builder()
                                        .latitude(7.444381)
                                        .longitude(46.948848)
                                        .build()
                        )
                        .build(),
                ScareEvaluationDto.builder()
                        .areaName("Zollikofen")
                        .numberOfArticles(60)
                        .areaPopulation(0)
                        .coordinates(
                                CoordinatesDto.builder()
                                        .latitude(7.452899)
                                        .longitude(46.997327)
                                        .build()
                        )
                        .build(),
                ScareEvaluationDto.builder()
                        .areaName("Zürich")
                        .numberOfArticles(300)
                        .areaPopulation(0)
                        .coordinates(
                                CoordinatesDto.builder()
                                        .latitude(8.530951)
                                        .longitude(47.381011)
                                        .build()
                        )
                        .build(),
                ScareEvaluationDto.builder()
                        .areaName("Thun")
                        .numberOfArticles(19)
                        .areaPopulation(0)
                        .coordinates(
                                CoordinatesDto.builder()
                                        .latitude(7.615548)
                                        .longitude(46.754203)
                                        .build()
                        )
                        .build()
        ).collect(Collectors.toList());
    }
}
