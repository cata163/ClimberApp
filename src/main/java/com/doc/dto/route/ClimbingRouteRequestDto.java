package com.doc.dto.route;

import com.doc.model.climb.ClimbGrade;
import com.doc.model.climb.ClimbType;

public record ClimbingRouteRequestDto(
    String name, String description, LocationDto location, ClimbGrade grade, ClimbType climbType) {}
