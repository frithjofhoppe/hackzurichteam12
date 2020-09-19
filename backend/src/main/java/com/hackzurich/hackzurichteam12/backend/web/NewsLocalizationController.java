package com.hackzurich.hackzurichteam12.backend.web;

import com.hackzurich.hackzurichteam12.backend.api.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/localization")
public class NewsLocalizationController {

    private final LocationRecognizerService locationRecognizerService;

    public NewsLocalizationController(LocationRecognizerService locationRecognizerService) {
        this.locationRecognizerService = locationRecognizerService;
    }

    @PostMapping("messages")
    public LocationRecognitionResult locateNews(@RequestBody CitySearchDto dto) {
        return locationRecognizerService.findCityCoordinatesByMessage(dto.getNews());
    }

    @PostMapping("coordinates")
    public LocationRecognitionResult locatePlace(@RequestBody CitySearchDto dto) {
        return locationRecognizerService.findCityByCoordinates(dto.getLatitude(), dto.getLongitude());
    }
}
