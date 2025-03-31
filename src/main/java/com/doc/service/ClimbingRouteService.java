package com.doc.service;

import com.doc.model.climb.ClimbingRoute;
import com.doc.repository.ClimbingRouteRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ClimbingRouteService {
  private final ClimbingRouteRepository repository;

  public ClimbingRouteService(ClimbingRouteRepository repository) {
    this.repository = repository;
  }

  public List<ClimbingRoute> findAll(Specification<ClimbingRoute> spec) {
    return repository.findAll(spec);
  }

  public Optional<ClimbingRoute> findById(Long routeId) {
    return repository.findById(routeId);
  }

  public Page<ClimbingRoute> findAll(Specification<ClimbingRoute> spec, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return repository.findAll(spec, pageable);
  }

  public void delete(ClimbingRoute route) {
    repository.delete(route);
  }

  public ClimbingRoute save(ClimbingRoute route) {
    return repository.save(route);
  }
}
