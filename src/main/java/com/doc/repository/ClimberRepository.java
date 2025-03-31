package com.doc.repository;

import com.doc.model.user.Climber;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClimberRepository extends JpaRepository<Climber, Long> {
  @Query("SELECT c FROM Climber c WHERE lower(c.username) LIKE lower(concat('%', :username, '%'))")
  List<Climber> findClimbersByUsername(@Param("username") String username);

  Optional<Climber> findByEmail(String email);

  Optional<Climber> findByCognitoSub(String sub);
}
