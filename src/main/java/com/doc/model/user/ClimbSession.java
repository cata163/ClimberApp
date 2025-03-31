package com.doc.model.user;

import com.doc.model.climb.ClimbingRoute;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions")
public class ClimbSession {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_seq")
  @SequenceGenerator(name = "session_seq", sequenceName = "session_sequence", allocationSize = 1)
  private Long id;

  private LocalDateTime start;

  private String notes;

  @ManyToMany
  @JoinTable(
      name = "session_routes",
      joinColumns = @JoinColumn(name = "session_id"),
      inverseJoinColumns = @JoinColumn(name = "climbing_route_id"))
  private List<ClimbingRoute> routes = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "climber_id", nullable = false)
  private Climber climber;
}
