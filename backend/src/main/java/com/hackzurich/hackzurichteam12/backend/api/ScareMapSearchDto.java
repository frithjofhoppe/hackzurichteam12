package com.hackzurich.hackzurichteam12.backend.api;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ScareMapSearchDto{
    CoordinatesDto southWest;
    CoordinatesDto northEast;
    String city;
}
