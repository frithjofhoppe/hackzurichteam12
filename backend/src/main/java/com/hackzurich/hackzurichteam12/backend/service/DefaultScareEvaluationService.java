package com.hackzurich.hackzurichteam12.backend.service;

import com.hackzurich.hackzurichteam12.backend.api.*;
import com.hackzurich.hackzurichteam12.backend.model.entity.LocationEntity;
import com.hackzurich.hackzurichteam12.backend.model.repo.LocationRepository;
import com.hackzurich.hackzurichteam12.backend.model.repo.NewsArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultScareEvaluationService implements ScareEvaluationService {

    private final LocationRecognizerService locationRecognizerService;

    private final NewsArticleRepository articleRepository;

    private final LocationRepository locationRepository;

    public DefaultScareEvaluationService(LocationRecognizerService locationRecognizerService, NewsArticleRepository articleRepository, LocationRepository locationRepository) {
        this.locationRecognizerService = locationRecognizerService;
        this.articleRepository = articleRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<ScareEvaluationDto> getEvaluations() {
        return locationRepository.findAll().stream().map(this::convertLocationToScareDto).collect(Collectors.toList());

    }

    private ScareEvaluationDto convertLocationToScareDto(LocationEntity location) {
        return ScareEvaluationDto.builder()
                .areaName(location.getCity())
                .numberOfArticles((int) articleRepository.countByLocationId(location.getId()))
                .coordinates(CoordinatesDto.builder().
                        longitude(location.getLongitude())
                        .latitude(location.getLatitude())
                        .build())
                .areaPopulation(0)
                .build();
    }

    @Override
    public List<ScareEvaluationDto> searchInScareMap(ScareMapSearchDto searchDto) {
        return locationRepository.findAllByLongitudeBetweenAndLatitudeBetween(
                searchDto.getSouthWest().getLongitude(),
                searchDto.getNorthEast().getLongitude(),
                searchDto.getSouthWest().getLatitude(),
                searchDto.getNorthEast().getLatitude())
                .stream()
                .map(this::convertLocationToScareDto)
                .collect(Collectors.toList());
    }
}
