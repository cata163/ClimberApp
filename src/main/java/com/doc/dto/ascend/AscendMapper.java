package com.doc.dto.ascend;

import com.doc.model.user.Ascend;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class AscendMapper {

  public ClimberAscendsResponseDto mapToClimberAscendsResponse(
      Long climberId, Page<Ascend> ascendsPage) {
    List<AscendDto> ascendsDto =
        ascendsPage.getContent().stream().map(this::mapAscend).collect(Collectors.toList());
    return new ClimberAscendsResponseDto(
        climberId,
        ascendsDto,
        ascendsPage.getNumber(),
        ascendsPage.getTotalPages(),
        ascendsPage.getTotalElements());
  }

  public AscendResponseDto mapToAscendResponse(Ascend ascend) {
    return new AscendResponseDto(
        ascend.getClimber().getId(),
        ascend.getClimber().getUsername(),
        ascend.getRoute().getId(),
        ascend.getRoute().getName(),
        ascend.getClimbedOn(),
        ascend.getProposedGrade());
  }

  private AscendDto mapAscend(Ascend ascend) {
    return new AscendDto(
        ascend.getRoute().getId(),
        ascend.getRoute().getName(),
        ascend.getClimbedOn(),
        ascend.getProposedGrade());
  }
}
