package com.hackzurich.hackzurichteam12.backend.api;

import java.util.List;

public interface LocationRecognizerService {
    List<IBMTextEntity> findLocationInNews(String message);

    LocationRecognitionResult lookupLocation(String location);

    LocationRecognitionResult findCityCoordinates(String message);
}
