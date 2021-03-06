package com.hackzurich.hackzurichteam12.backend.api;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MapboxGeometry {
    String type;
    double[] coordinates;
}
