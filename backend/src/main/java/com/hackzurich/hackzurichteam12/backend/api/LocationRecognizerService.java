package com.hackzurich.hackzurichteam12.backend.api;

import java.util.List;

public interface LocationRecognizerService {
    List<IBMTextEntity> findLocationInNews(String message);

    LocationRecognitionResult lookupLocation(String location);

    /**
     * Extracts the city and its geo coordinates of a news message
     * @param message
     * @return
     */
    LocationRecognitionResult findCityCoordinatesByMessage(String message);

    LocationRecognitionResult findCityByCoordinates(double latitude, double longitude);
}
