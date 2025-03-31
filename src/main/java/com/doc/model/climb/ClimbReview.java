package com.doc.model.climb;


import com.doc.model.user.Climber;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reviews")
public class ClimbReview {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "climber_id", nullable = false)
  private Climber climber;

  @ManyToOne
  @JoinColumn(name = "climbing_route_id", nullable = false)
  private ClimbingRoute climbingRoute;

  private String reviewText;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"))
  @Column(name = "image_url")
  private List<String> imageUrls = new ArrayList<>();

}
