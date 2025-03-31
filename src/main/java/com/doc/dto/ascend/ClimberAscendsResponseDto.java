package com.doc.dto.ascend;

import java.util.List;

public record ClimberAscendsResponseDto(
    Long climberId, List<AscendDto> ascends, int currentPage, int totalPages, long totalAscends) {}
