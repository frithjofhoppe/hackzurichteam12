package com.hackzurich.hackzurichteam12.backend.api;

public interface LocationRecognizerService {
    IBMTextEntity findLocationInNews(String message);

    LocationRecognitionResult getCoordinatesOfLocation(String location);
}
