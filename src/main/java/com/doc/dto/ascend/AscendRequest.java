package com.doc.dto.ascend;

import com.doc.model.climb.ClimbGrade;
import java.time.LocalDateTime;

public record AscendRequest(LocalDateTime climbedOn, Long routeId, ClimbGrade proposedGrade) {}
