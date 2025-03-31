package com.doc.dto.climber;

import com.doc.model.climb.ClimbGrade;

public record ClimberRequestDto(
    String username,
    ClimbGrade climbGrade
) {}
