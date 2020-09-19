package com.hackzurich.hackzurichteam12.backend.api;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationRecognitionResult {
    String city;
    CoordinatesDto coordinates;
}
