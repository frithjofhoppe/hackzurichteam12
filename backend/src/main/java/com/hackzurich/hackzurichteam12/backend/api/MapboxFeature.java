package com.hackzurich.hackzurichteam12.backend.api;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MapboxFeature {
    String id;
    String text;
    String place_name;
    MapboxGeometry geometry;
}
