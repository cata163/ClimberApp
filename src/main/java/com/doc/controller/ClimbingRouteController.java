package com.doc.controller;

import com.doc.dto.route.ClimbingRouteDto;
import com.doc.dto.route.ClimbingRouteMapper;
import com.doc.dto.route.ClimbingRoutePageDto;
import com.doc.dto.route.ClimbingRouteRequestDto;
import com.doc.exception.ResourceNotFoundException;
import com.doc.model.climb.ClimbingRoute;
import com.doc.rsql.CustomRsqlOperators;
import com.doc.rsql.CustomRsqlVisitor;
import com.doc.service.ClimbingRouteService;
import com.doc.service.S3Service;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/climbing-routes")
public class ClimbingRouteController {

  private final ClimbingRouteService service;
  private final ClimbingRouteMapper mapper;
  private final S3Service s3Service;

  public ClimbingRouteController(
      ClimbingRouteService service, ClimbingRouteMapper mapper, S3Service s3Service) {
    this.service = service;
    this.mapper = mapper;
    this.s3Service = s3Service;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ClimbingRouteDto> createRoute(
      @RequestPart("route") ClimbingRouteRequestDto routeRequestDto,
      @RequestPart(value = "images", required = false) List<MultipartFile> images) {
    List<String> imageIds = uploadImages(images);
    ClimbingRoute route = mapper.mapToEntity(routeRequestDto);
    route.setImageUrls(imageIds);
    ClimbingRoute savedRoute = service.save(route);
    ClimbingRouteDto responseDto = mapper.mapClimbingRoute(savedRoute);

    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  private List<String> uploadImages(List<MultipartFile> images) {
    List<String> imageIds = new ArrayList<>();
    if (images != null) {
      for (MultipartFile image : images) {
        if (!image.isEmpty()) {
          String imageId = s3Service.uploadFile(image);
          imageIds.add(imageId);
        }
      }
    }
    return imageIds;
  }

  @GetMapping("/{id}")
  public ResponseEntity<ClimbingRouteDto> filterClimbingRoutes(@PathVariable Long id)
      throws ResourceNotFoundException {
    ClimbingRoute route =
        service
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("Climbing Route not found with id " + id));
    return ResponseEntity.ok(mapper.mapClimbingRoute(route));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteClimbingRoute(@PathVariable Long id)
      throws ResourceNotFoundException {
    ClimbingRoute route =
        service
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("Climbing Route not found with id " + id));
    service.delete(route);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<ClimbingRoutePageDto> filterClimbingRoutes(
      @RequestParam(name = "filter") String filter,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "50") int size) {
    Node rootNode = new RSQLParser(CustomRsqlOperators.defaultOperators()).parse(filter);
    Specification<ClimbingRoute> spec = rootNode.accept(new CustomRsqlVisitor());
    Page<ClimbingRouteDto> pagedRoutes =
        service.findAll(spec, page, size).map(mapper::mapClimbingRoute);
    return ResponseEntity.ok(
        new ClimbingRoutePageDto(
            pagedRoutes.getContent(),
            pagedRoutes.getNumber(),
            pagedRoutes.getTotalPages(),
            pagedRoutes.getNumberOfElements()));
  }
}
