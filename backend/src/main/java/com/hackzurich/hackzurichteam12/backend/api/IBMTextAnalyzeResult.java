package com.hackzurich.hackzurichteam12.backend.api;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IBMTextAnalyzeResult {
    String language;
    List<IBMTextEntity> entities;
}
