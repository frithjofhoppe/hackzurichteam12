package com.hackzurich.hackzurichteam12.backend.api;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScareEvaluationDto {
    CoordinatesDto coordinates;
    int numberOfArticles;
    String areaName;
    int areaPopulation;
}
