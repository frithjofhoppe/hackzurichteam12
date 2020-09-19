package com.hackzurich.hackzurichteam12.backend.api;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationRecognitionResult {
    int occurrences;
    List<Location> locationList;
}
