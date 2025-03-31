package com.doc.repository;

import com.doc.model.user.Ascend;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AscendRepository extends JpaRepository<Ascend, Long> {

  @Query("SELECT a FROM Ascend a WHERE a.climber.id = :climberId")
  List<Ascend> findAllByClimberId(@Param("climberId") Long climberId);

  @Query("SELECT a FROM Ascend a WHERE a.climber.id = :climberId")
  Page<Ascend> findAllByClimberId(@Param("climberId") Long climberId, Pageable pageable);
}
