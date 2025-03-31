package com.doc.service;

import com.doc.model.user.Ascend;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AscendService {

  List<Ascend> getAscendsForClimber(Long climberId);

  Page<Ascend> getAscendsForClimber(Long climberId, int pageNum, int pageSize);

  Page<Ascend> getAscendsForClimber(
      Long climberId, int page, int size, String sortBy, String sortDirection);

  void delete(Long ascendId);

  Ascend save(Ascend ascend);

  Optional<Ascend> findById(Long ascendId);

  Ascend updateAscend(Ascend ascend);
}
