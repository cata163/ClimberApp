package com.doc.dto.route;

import com.doc.model.climb.ClimbingRoute;
import com.doc.model.common.Location;
import com.doc.service.S3Service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ClimbingRouteMapper {
  private final S3Service s3Service;

  public ClimbingRouteMapper(S3Service s3Service) {
    this.s3Service = s3Service;
  }

  public ClimbingRouteDto mapClimbingRoute(ClimbingRoute route) {
    return new ClimbingRouteDto(
        route.getId(),
        route.getName(),
        route.getDescription(),
        mapLocation(route.getLocation()),
        route.getGrade(),
        route.getClimbType(),
        createImageSignedUrl(route.getImageUrls()));
  }

  private List<String> createImageSignedUrl(List<String> imageUrls) {
    return imageUrls.stream()
        .map(image -> s3Service.generateSignedUrl(image))
        .collect(Collectors.toList());
  }

  private LocationDto mapLocation(Location location) {
    return new LocationDto(location.getLatitude(), location.getLongitude());
  }

  public ClimbingRoute mapToEntity(ClimbingRouteRequestDto routeRequestDto) {
    ClimbingRoute route = new ClimbingRoute();
    route.setName(routeRequestDto.name());
    route.setClimbType(routeRequestDto.climbType());
    route.setDescription(routeRequestDto.description());
    route.setGrade(routeRequestDto.grade());
    route.setLocation(
        new Location(
            routeRequestDto.location().longitude(), routeRequestDto.location().latitude()));
    return route;
  }
}
