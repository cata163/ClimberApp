package com.doc.model.user;

import com.doc.model.climb.ClimbGrade;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "climbers")
public class Climber {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "climber_seq")
  @SequenceGenerator(name = "climber_seq", sequenceName = "climber_sequence", allocationSize = 1)
  private Long id;

  private String username;
  private String email;

  private String cognitoSub;
  private String profileImage;

  @Enumerated(EnumType.STRING)
  private ClimbGrade climbingGrade;

  @OneToMany(mappedBy = "climber", cascade = CascadeType.ALL)
  private Collection<ClimbSession> sessions = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getProfileImage() {
    return profileImage;
  }

  public void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }

  public ClimbGrade getClimbingGrade() {
    return climbingGrade;
  }

  public void setClimbingGrade(ClimbGrade climbingGrade) {
    this.climbingGrade = climbingGrade;
  }

  public Collection<ClimbSession> getSessions() {
    return sessions;
  }

  public void setSessions(Collection<ClimbSession> sessions) {
    this.sessions = sessions;
  }

  public String getCognitoSub() {
    return cognitoSub;
  }

  public void setCognitoSub(String cognitoSub) {
    this.cognitoSub = cognitoSub;
  }
}
