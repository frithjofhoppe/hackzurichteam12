package com.hackzurich.hackzurichteam12.backend.api;

public interface LocationRecognizerService {
    IBMTextEntity findLocationInNews(String message);

    LocationRecognitionResult lookupLocation(String location);

    LocationRecognitionResult findCityCoordinates(String message);
}
