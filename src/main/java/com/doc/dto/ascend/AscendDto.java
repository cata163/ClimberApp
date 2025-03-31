package com.doc.dto.ascend;

import com.doc.model.climb.ClimbGrade;
import java.time.LocalDateTime;

public record AscendDto(
    Long routeId, String routeName, LocalDateTime climbedOn, ClimbGrade proposedGrade) {}
