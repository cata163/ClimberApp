package com.doc.dto.climber;

import com.doc.model.user.Climber;
import com.doc.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClimberDtoMapper {
  private S3Service s3Service;

  public ClimberDtoMapper(S3Service s3Service) {
    this.s3Service = s3Service;
  }

  public ClimberResponseDto mapClimber(Climber climber) {
    return new ClimberResponseDto(
        s3Service.generateSignedUrl(climber.getProfileImage()),
        climber.getUsername(),
        climber.getClimbingGrade().getGrade());
  }
}
