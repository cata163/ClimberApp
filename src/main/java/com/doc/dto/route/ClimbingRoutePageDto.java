package com.doc.dto.route;

import java.util.List;

public record ClimbingRoutePageDto(
    List<ClimbingRouteDto> ascends, int currentPage, int totalPages, long totalRoutes) {}
