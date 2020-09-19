package com.hackzurich.hackzurichteam12.backend.api;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IBMTextAnalyzeRequest {
    String text;
    IBMTextFeature features;
}
