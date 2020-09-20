package com.hackzurich.hackzurichteam12.backend.api;

import java.util.List;

public interface ScareEvaluationService {
    /**
     * Get available evaluations (... later for specific period)
     * @return
     */
    public List<ScareEvaluationDto> getEvaluations();

    /**
     * Searches in the scare map after geographic limitation and city
     * @param searchDto
     * @return
     */
    public List<ScareEvaluationDto> searchInScareMap(ScareMapSearchDto searchDto);
}
