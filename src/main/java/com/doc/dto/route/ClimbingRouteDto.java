package com.doc.dto.route;

import com.doc.model.climb.ClimbGrade;
import com.doc.model.climb.ClimbType;
import java.util.List;

public record ClimbingRouteDto(
    Long id,
    String name,
    String description,
    LocationDto location,
    ClimbGrade grade,
    ClimbType climbType,
    List<String> images) {}
