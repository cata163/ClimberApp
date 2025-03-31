package com.doc.controller;

import com.doc.dto.ascend.AscendMapper;
import com.doc.dto.ascend.ClimberAscendsResponseDto;
import com.doc.dto.climber.ClimberDtoMapper;
import com.doc.dto.climber.ClimberPageResponseDto;
import com.doc.dto.climber.ClimberRequestDto;
import com.doc.dto.climber.ClimberResponseDto;
import com.doc.model.user.Ascend;
import com.doc.model.user.Climber;
import com.doc.service.AscendService;
import com.doc.service.ClimberService;
import com.doc.service.S3Service;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/climbers")
public class ClimberController {
  private final ClimberService climberService;

  private final AscendService ascendService;

  private final ClimberDtoMapper climberMapper;
  private final AscendMapper ascendMapper;
  private final S3Service s3Service;

  public ClimberController(
      ClimberService climberService,
      AscendService ascendService,
      ClimberDtoMapper mapper,
      AscendMapper ascendMapper,
      S3Service s3Service) {
    this.climberService = climberService;
    this.ascendService = ascendService;
    this.climberMapper = mapper;
    this.ascendMapper = ascendMapper;
    this.s3Service = s3Service;
  }

  @GetMapping
  public ResponseEntity<ClimberPageResponseDto> listAllClimbers(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
    Page<ClimberResponseDto> climbers =
        climberService.findAll(page, size).map(climber -> climberMapper.mapClimber(climber));
    ClimberPageResponseDto pageResponseDto =
        new ClimberPageResponseDto(
            climbers.toList(),
            climbers.getNumber(),
            climbers.getTotalPages(),
            climbers.getTotalElements());
    return ResponseEntity.ok(pageResponseDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ClimberResponseDto> getClimber(@PathVariable(value = "id") Long climberId) {
    Optional<Climber> climber = climberService.findById(climberId);
    if (climber.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Climber not found");
    }
    return ResponseEntity.ok().body(climberMapper.mapClimber(climber.get()));
  }

  @GetMapping("/{id}/ascends")
  public ResponseEntity<ClimberAscendsResponseDto> getAscends(
      @PathVariable(value = "id") Long climberId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "50") int size,
      @RequestParam(defaultValue = "climbedOn") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDirection) {
    Page<Ascend> ascendsForClimber =
        ascendService.getAscendsForClimber(climberId, page, size, sortBy, sortDirection);
    return ResponseEntity.ok()
        .body(ascendMapper.mapToClimberAscendsResponse(climberId, ascendsForClimber));
  }

  @PostMapping(path = "/profile")
  public ResponseEntity<ClimberResponseDto> createClimberProfile(
      @AuthenticationPrincipal Jwt jwt,
      @RequestPart("climber") ClimberRequestDto climberRequest,
      @RequestPart("profileImageFile") MultipartFile profileImageFile) {
    String imageId = this.s3Service.uploadFile(profileImageFile);
    Climber climber = new Climber();
    climber.setClimbingGrade(climberRequest.climbGrade());
    climber.setEmail(jwt.getClaim("email"));
    climber.setUsername(climberRequest.username());
    climber.setProfileImage(imageId);
    return ResponseEntity.ok().body(climberMapper.mapClimber(climber));
  }

  @GetMapping(path = "/profile")
  public ResponseEntity<ClimberResponseDto> createClimberProfile(@AuthenticationPrincipal Jwt jwt) {
    Climber climber =
        this.climberService
            .findBySub(jwt.getClaim("sub"))
            .orElseThrow(
                () ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Climber Profile not found"));
    return ResponseEntity.ok().body(climberMapper.mapClimber(climber));
  }

  @DeleteMapping(path = "/profile")
  public ResponseEntity<ClimberResponseDto> deleteClimbingProfile(
      @AuthenticationPrincipal Jwt jwt) {
    Climber climber =
        this.climberService
            .findBySub(jwt.getClaim("sub"))
            .orElseThrow(
                () ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Climber Profile not found"));
    this.climberService.delete(climber);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping(path = "/profile")
  public ResponseEntity<ClimberResponseDto> updateClimberProfile(
      @AuthenticationPrincipal Jwt jwt,
      @RequestPart(value = "climber", required = false) ClimberRequestDto climberRequest,
      @RequestPart(value = "profileImageFile", required = false) MultipartFile profileImageFile) {
    Climber climber = fetchClimber(jwt);
    updateClimber(climberRequest, profileImageFile, climber);
    Climber updatedClimber = climberService.updateClimber(climber);
    return ResponseEntity.ok(climberMapper.mapClimber(updatedClimber));
  }

  private void updateClimber(
      ClimberRequestDto climberRequest, MultipartFile profileImageFile, Climber climber) {
    if (climberRequest != null) {
      if (climberRequest.username() != null) {
        climber.setUsername(climberRequest.username());
      }
      if (climberRequest.climbGrade() != null) {
        climber.setClimbingGrade(climberRequest.climbGrade());
      }
    }

    if (profileImageFile != null && !profileImageFile.isEmpty()) {
      String imageId = s3Service.uploadFile(profileImageFile);
      climber.setProfileImage(imageId);
    }
  }

  private Climber fetchClimber(Jwt jwt) {
    String email = jwt.getClaim("email");
    return climberService
        .findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Climber not found"));
  }
}
