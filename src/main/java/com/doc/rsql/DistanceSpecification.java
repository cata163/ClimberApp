package com.doc.rsql;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class DistanceSpecification<T> implements Specification<T> {
  private final double R = 6371.0;
  private Double lat;
  private Double lon;
  private Double maxDistance;

  public DistanceSpecification(Double lat, Double lon, Double maxDistance) {
    this.lat = lat;
    this.lon = lon;
    this.maxDistance = maxDistance;
  }

  @Override
  public Predicate toPredicate(
      Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    final double R = 6371.0;

    Expression<Double> distance = calculateDistanceUsingHarversine(root, builder, R);

    return builder.lessThanOrEqualTo(distance, maxDistance);
  }

  private Expression<Double> calculateDistanceUsingHarversine(
      Root<T> root, CriteriaBuilder builder, double R) {
    Expression<Double> givenLatRad = builder.literal(Math.toRadians(lat));
    Expression<Double> givenLonRad = builder.literal(Math.toRadians(lon));

    Expression<Double> routeLat = root.get("location").get("latitude");
    Expression<Double> routeLon = root.get("location").get("longitude");

    Expression<Double> routeLatRad = builder.function("radians", Double.class, routeLat);
    Expression<Double> routeLonRad = builder.function("radians", Double.class, routeLon);

    Expression<Double> latDiff = builder.diff(routeLatRad, givenLatRad);
    Expression<Double> lonDiff = builder.diff(routeLonRad, givenLonRad);

    Expression<Double> sinLatDiff =
        builder.function("sin", Double.class, builder.quot(latDiff, builder.literal(2.0)));
    Expression<Double> sinLatDiffSquared = builder.prod(sinLatDiff, sinLatDiff);

    Expression<Double> sinLonDiff =
        builder.function("sin", Double.class, builder.quot(lonDiff, builder.literal(2.0)));
    Expression<Double> sinLonDiffSquared = builder.prod(sinLonDiff, sinLonDiff);

    Expression<Double> cosGivenLat = builder.function("cos", Double.class, givenLatRad);
    Expression<Double> cosRouteLat = builder.function("cos", Double.class, routeLatRad);
    Expression<Double> cosProduct = builder.prod(cosGivenLat, cosRouteLat);

    // Calculate the 'a' component of the Haversine formula:
    // a = sin^2(deltaLat/2) + cos(givenLat) * cos(routeLatRad) * sin^2(deltaLon/2)
    Expression<Double> a =
        builder.sum(sinLatDiffSquared, builder.prod(cosProduct, sinLonDiffSquared));

    // Calculate the 'c' component: c = 2 * atan2(sqrt(a), sqrt(1 - a))
    Expression<Double> sqrtA = builder.function("sqrt", Double.class, a);
    Expression<Double> sqrtOneMinusA =
        builder.function("sqrt", Double.class, builder.diff(builder.literal(1.0), a));
    Expression<Double> atan2 = builder.function("atan2", Double.class, sqrtA, sqrtOneMinusA);
    Expression<Double> c = builder.prod(builder.literal(2.0), atan2);

    // Calculate the distance: distance = R * c
    return  builder.prod(builder.literal(R), c);
  }
}
