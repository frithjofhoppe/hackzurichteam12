package com.hackzurich.hackzurichteam12.backend.api;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IBMTextEntity {
    String type;
    String text;
    String relevance;
    int count;
}
