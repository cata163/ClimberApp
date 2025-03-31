package com.doc.dto.climber;

import java.util.List;

public record ClimberPageResponseDto(
    List<ClimberResponseDto> climbers, int currentPage, int totalPages, long totalClimbers) {}
