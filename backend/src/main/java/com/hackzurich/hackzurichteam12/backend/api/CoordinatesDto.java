package com.hackzurich.hackzurichteam12.backend.api;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoordinatesDto {
    double latitude;
    double longitude;
}
