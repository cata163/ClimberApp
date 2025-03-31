package com.doc.rsql;

import com.doc.model.climb.ClimbingRoute;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;

public class ClimbingGradeRsqlSpecification implements Specification<ClimbingRoute> {
  private final String property;
  private final ComparisonOperator operator;
  private final List<String> arguments;

  public ClimbingGradeRsqlSpecification(
      String property, ComparisonOperator operator, List<String> arguments) {
    this.property = property;
    this.operator = operator;
    this.arguments = arguments;
  }

  private List<Object> castArguments(final Root<ClimbingRoute> root) {
    Class<? extends Object> type = root.get(property).getJavaType();
    List<Object> args =
        arguments.stream()
            .map(
                arg -> {
                  if (type.equals(Integer.class)) {
                    return Integer.parseInt(arg);
                  } else if (type.equals(Long.class)) {
                    return Long.parseLong(arg);
                  } else if (type.equals(Double.class)) {
                    return Double.parseDouble(arg);
                  } else {
                    return arg;
                  }
                })
            .collect(Collectors.toList());
    return args;
  }

  @Override
  public Predicate toPredicate(
      Root<ClimbingRoute> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    List<Object> args = castArguments(root);
    Object argument = args.get(0);
    switch (RsqlSearchOperation.getSimpleOperator(operator)) {
      case EQUAL:
        {
          return createEqualPredicate(root, builder, argument);
        }
      case NOT_EQUAL:
        {
          return createNotEqualPredicate(root, builder, argument);
        }
      case GREATER_THAN:
        {
          return builder.greaterThan(root.get(property), argument.toString());
        }
      case GREATER_THAN_OR_EQUAL:
        {
          return builder.greaterThanOrEqualTo(root.get(property), argument.toString());
        }
      case LESS_THAN:
        {
          return builder.lessThan(root.get(property), argument.toString());
        }
      case LESS_THAN_OR_EQUAL:
        {
          return builder.lessThanOrEqualTo(root.get(property), argument.toString());
        }
      case IN:
        return root.get(property).in(args);
      case NOT_IN:
        return builder.not(root.get(property).in(args));
      case IN_CIRCLE:
        return createInCirclePredicate(root, query, builder, args);
    }

    return null;
  }

  private Predicate createInCirclePredicate(
      Root<ClimbingRoute> root,
      CriteriaQuery<?> query,
      CriteriaBuilder builder,
      List<Object> args) {
    try {
      DistanceSpecification distanceSpecification =
          new DistanceSpecification(
              Double.parseDouble(args.get(0).toString()),
              Double.parseDouble(args.get(1).toString()),
              Double.parseDouble(args.get(2).toString()));
      return distanceSpecification.toPredicate(root, query, builder);
    } catch (NumberFormatException | IndexOutOfBoundsException e) {
      throw new InvalidRsqlException();
    }
  }

  private Predicate createEqualPredicate(
      Root<ClimbingRoute> root, CriteriaBuilder builder, Object argument) {
    if (argument instanceof String) {
      return builder.like(root.get(property), argument.toString().replace('*', '%'));
    } else if (argument == null) {
      return builder.isNull(root.get(property));
    } else {
      return builder.equal(root.get(property), argument);
    }
  }

  private Predicate createNotEqualPredicate(
      Root<ClimbingRoute> root, CriteriaBuilder builder, Object argument) {
    if (argument instanceof String) {
      return builder.notLike(root.get(property), argument.toString().replace('*', '%'));
    } else if (argument == null) {
      return builder.isNotNull(root.get(property));
    } else {
      return builder.notEqual(root.get(property), argument);
    }
  }
}
