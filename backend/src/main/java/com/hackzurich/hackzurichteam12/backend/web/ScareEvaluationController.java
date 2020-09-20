package com.hackzurich.hackzurichteam12.backend.web;

import com.hackzurich.hackzurichteam12.backend.api.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scareEvaluations")
public class ScareEvaluationController {

    private final ScareEvaluationService scareEvaluationService;

    public ScareEvaluationController(ScareEvaluationService scareEvaluationService, LocationRecognizerService locationRecognizerService) {
        this.scareEvaluationService = scareEvaluationService;
    }

    @GetMapping
    public List<ScareEvaluationDto> getEvaluations() {
        return scareEvaluationService.getEvaluations();
    }

    @PostMapping
    public List<ScareEvaluationDto> searchScareMap(@RequestBody ScareMapSearchDto searchDto) {
        return scareEvaluationService.searchInScareMap(searchDto);
    }
}
