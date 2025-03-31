package com.doc.controller;

import com.doc.dto.ascend.AscendMapper;
import com.doc.dto.ascend.AscendPatchDto;
import com.doc.dto.ascend.AscendRequest;
import com.doc.dto.ascend.AscendResponseDto;
import com.doc.exception.ResourceNotFoundException;
import com.doc.model.climb.ClimbingRoute;
import com.doc.model.user.Ascend;
import com.doc.model.user.Climber;
import com.doc.service.AscendService;
import com.doc.service.ClimberService;
import com.doc.service.ClimbingRouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/ascends")
public class AscendController {
  private final AscendService ascendService;
  private final ClimberService climberService;
  private final ClimbingRouteService climbingRouteService;
  private final AscendMapper mapper;

  public AscendController(
      AscendService ascendService,
      ClimberService climberService,
      ClimbingRouteService climbingRouteService,
      AscendMapper mapper) {
    this.ascendService = ascendService;
    this.climberService = climberService;
    this.climbingRouteService = climbingRouteService;
    this.mapper = mapper;
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deleteAscend(@PathVariable(name = "id") Long ascendId,
      @AuthenticationPrincipal Jwt jwt) throws ResourceNotFoundException {
    String email = jwt.getClaim("email");
    Ascend ascend =
        ascendService
            .findById(ascendId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Ascend not found with id " + ascendId));
    if (!ascend.getClimber().getEmail().equalsIgnoreCase(email)) {
      throw new AccessDeniedException("Not authorized to update this ascend");
    }
    ascendService.delete(ascendId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<AscendResponseDto> getAscend(@PathVariable(name = "id") Long ascendId)
      throws ResourceNotFoundException {
    Ascend ascend =
        ascendService
            .findById(ascendId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Ascend not found with id " + ascendId));
    return ResponseEntity.ok(mapper.mapToAscendResponse(ascend));
  }

  @PatchMapping(path = "/{id}")
  public ResponseEntity<AscendResponseDto> updateAscend(
      @PathVariable("id") Long ascendId,
      @AuthenticationPrincipal Jwt jwt,
      @RequestBody AscendPatchDto patchDto)
      throws ResourceNotFoundException {
    String email = jwt.getClaim("email");
    Ascend ascend =
        ascendService
            .findById(ascendId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Ascend not found with id " + ascendId));
    if (!ascend.getClimber().getEmail().equalsIgnoreCase(email)) {
      throw new AccessDeniedException("Not authorized to update this ascend");
    }
    if (patchDto.proposedGrade() != null) {
      ascend.setProposedGrade(patchDto.proposedGrade());
    }
    Ascend updatedAscend = ascendService.updateAscend(ascend);
    return ResponseEntity.ok(mapper.mapToAscendResponse(updatedAscend));
  }

  @PostMapping
  public ResponseEntity<AscendResponseDto> createAscend(
      @AuthenticationPrincipal Jwt jwt, @RequestBody AscendRequest request)
      throws ResourceNotFoundException {
    Climber climber =
        climberService
            .findByEmail(jwt.getClaimAsString("email"))
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Climber not found with email " + jwt.getClaim("email")));
    ClimbingRoute route =
        climbingRouteService
            .findById(request.routeId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException("Route not found with id " + request.routeId()));
    Ascend ascend = new Ascend(route, climber);
    ascend.setClimbedOn(request.climbedOn());
    ascend.setProposedGrade(request.proposedGrade());
    ascend = ascendService.save(ascend);
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapToAscendResponse(ascend));
  }
}
