package com.doc.service;

import com.doc.model.user.Climber;
import com.doc.repository.ClimberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClimberServiceImpl implements ClimberService {
  private final ClimberRepository climberRepository;

  public ClimberServiceImpl(ClimberRepository climberRepository) {
    this.climberRepository = climberRepository;
  }

  @Override
  public List<Climber> findAll() {
    return climberRepository.findAll();
  }

  @Override
  public List<Climber> filterByUsername(String username) {
    return climberRepository.findClimbersByUsername(username);
  }

  @Override
  public Climber save(Climber climber) {
    return climberRepository.save(climber);
  }

  @Override
  public Optional<Climber> findByEmail(String email) {
    return climberRepository.findByEmail(email);
  }

  @Override
  public Climber updateClimber(Climber climber) {
    return climberRepository.save(climber);
  }

  @Override
  public Optional<Climber> findById(Long climberId) {
    return climberRepository.findById(climberId);
  }

  @Override
  public Optional<Climber> findBySub(String sub) {
    return climberRepository.findByCognitoSub(sub);
  }

  @Override
  public Page<Climber> findAll(int pageNum, int size) {
    Pageable page = PageRequest.of(pageNum, size);
    return climberRepository.findAll(page);
  }

  @Override
  public void delete(Climber climber) {
    climberRepository.delete(climber);
  }
}
