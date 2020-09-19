package com.hackzurich.hackzurichteam12.backend.api;

public interface LocationRecognizerService {
    LocationRecognitionResult findLocationInMessage(String message);
}
