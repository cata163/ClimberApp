package com.doc.model.user;

import com.doc.model.climb.ClimbGrade;
import com.doc.model.climb.ClimbingRoute;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "ascends",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"CLIMBINGROUTE_ID", "CLIMBER_ID"})})
public class Ascend {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ascend_seq")
  @SequenceGenerator(name = "ascend_seq", sequenceName = "ascend_sequence", allocationSize = 1)
  private Long id;

  @Column(updatable = false)
  private LocalDateTime climbedOn;

  @ManyToOne
  @JoinColumn(name = "CLIMBINGROUTE_ID", insertable = false, updatable = false)
  private ClimbingRoute route;

  @ManyToOne
  @JoinColumn(name = "CLIMBER_ID", insertable = false, updatable = false)
  private Climber climber;

  @Enumerated(EnumType.STRING)
  private ClimbGrade proposedGrade;

  public Ascend() {}

  public Ascend(ClimbingRoute route, Climber climber) {
    this.route = route;
    this.climber = climber;
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getClimbedOn() {
    return climbedOn;
  }

  public void setClimbedOn(LocalDateTime climbedOn) {
    this.climbedOn = climbedOn;
  }

  public ClimbingRoute getRoute() {
    return route;
  }

  public Climber getClimber() {
    return climber;
  }

  public ClimbGrade getProposedGrade() {
    return proposedGrade;
  }

  public void setProposedGrade(ClimbGrade proposedGrade) {
    this.proposedGrade = proposedGrade;
  }
}
