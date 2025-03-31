package com.doc.repository;


import com.doc.model.climb.ClimbingRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClimbingRouteRepository extends JpaRepository<ClimbingRoute, Long>,
    JpaSpecificationExecutor {

}
