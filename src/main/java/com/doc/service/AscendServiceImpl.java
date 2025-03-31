package com.doc.service;

import com.doc.model.user.Ascend;
import com.doc.repository.AscendRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AscendServiceImpl implements AscendService {
  private final AscendRepository repository;

  public AscendServiceImpl(AscendRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Ascend> getAscendsForClimber(Long climberId) {
    return repository.findAllByClimberId(climberId);
  }

  @Override
  public Page<Ascend> getAscendsForClimber(Long climberId, int pageNum, int pageSize) {
    Pageable page = PageRequest.of(pageNum, pageSize);
    return repository.findAllByClimberId(climberId, page);
  }

  @Override
  public Page<Ascend> getAscendsForClimber(
      Long climberId, int pageNum, int size, String sortBy, String sortDirection) {
    Sort sort = Sort.by(getSortDirection(sortDirection).name(), sortBy);
    Pageable page = PageRequest.of(pageNum, size, sort);
    return repository.findAllByClimberId(climberId, page);
  }

  @Override
  public void delete(Long ascendId) {
    repository.deleteById(ascendId);
  }

  @Override
  public Ascend save(Ascend ascend) {
    return repository.save(ascend);
  }

  @Override
  public Optional<Ascend> findById(Long ascendId) {
    return repository.findById(ascendId);
  }

  @Override
  public Ascend updateAscend(Ascend ascend) {
    return repository.save(ascend);
  }

  private SortDirection getSortDirection(String sortDirection) {
    try {
      return SortDirection.interpret(sortDirection);
    } catch (IllegalArgumentException exception) {
      return SortDirection.ASCENDING;
    }
  }
}
