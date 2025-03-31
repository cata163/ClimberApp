package com.doc.service;

import com.doc.model.user.Climber;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ClimberService {
  List<Climber> findAll();

  List<Climber> filterByUsername(String username);

  Climber save(Climber climber);

  Optional<Climber> findByEmail(String email);

  Climber updateClimber(Climber climber);

  Optional<Climber> findById(Long climberId);

  Optional<Climber>  findBySub(String sub);

  Page<Climber> findAll(int page, int size);

  void delete(Climber climber);
}
