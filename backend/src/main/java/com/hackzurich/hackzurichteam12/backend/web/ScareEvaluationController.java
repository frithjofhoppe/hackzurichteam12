package com.hackzurich.hackzurichteam12.backend.web;

import com.hackzurich.hackzurichteam12.backend.api.ScareEvaluationDto;
import com.hackzurich.hackzurichteam12.backend.api.ScareEvaluationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scareEvaluations")
public class ScareEvaluationController {

    private final ScareEvaluationService scareEvaluationService;

    public ScareEvaluationController(ScareEvaluationService scareEvaluationService) {
        this.scareEvaluationService = scareEvaluationService;
    }

    @GetMapping
    public List<ScareEvaluationDto> getEvaluations() {
        return scareEvaluationService.getEvaluations();
    }
}
