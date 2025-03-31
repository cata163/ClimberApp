package com.doc.model.climb;

import com.doc.model.common.Location;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routes")
public class ClimbingRoute {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "climbing_route_seq")
  @SequenceGenerator(
      name = "climbing_route_seq",
      sequenceName = "climbing_route_sequence",
      allocationSize = 1)
  private Long id;

  private String name;
  private String description;
  @Embedded private Location location;

  @Enumerated(EnumType.STRING)
  private ClimbGrade grade;

  @Enumerated(EnumType.STRING)
  private ClimbType climbType;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"))
  @Column(name = "image_url")
  private List<String> imageUrls = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Location getLocation() {
    return location;
  }

  public ClimbGrade getGrade() {
    return grade;
  }

  public ClimbType getClimbType() {
    return climbType;
  }

  public List<String> getImageUrls() {
    return imageUrls;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public void setGrade(ClimbGrade grade) {
    this.grade = grade;
  }

  public void setClimbType(ClimbType climbType) {
    this.climbType = climbType;
  }

  public void setImageUrls(List<String> imageUrls) {
    this.imageUrls = imageUrls;
  }
}
