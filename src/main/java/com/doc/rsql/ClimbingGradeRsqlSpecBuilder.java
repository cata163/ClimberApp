package com.doc.rsql;

import com.doc.model.climb.ClimbingRoute;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.LogicalOperator;
import cz.jirutka.rsql.parser.ast.Node;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;

public class ClimbingGradeRsqlSpecBuilder {
  public Specification<ClimbingRoute> createSpecification(Node node) {
    if (node instanceof LogicalNode) {
      return createSpecification((LogicalNode) node);
    }
    if (node instanceof ComparisonNode) {
      return createSpecification((ComparisonNode) node);
    }
    return null;
  }

  public Specification<ClimbingRoute> createSpecification(LogicalNode logicalNode) {
    List<Specification> specs =
        logicalNode.getChildren().stream()
            .map(node -> createSpecification(node))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    Specification<ClimbingRoute> result = createSpecification(logicalNode, specs);
    return result;
  }

  public Specification<ClimbingRoute> createSpecification(ComparisonNode comparisonNode) {
    Specification<ClimbingRoute> result =
        Specification.where(
            new ClimbingGradeRsqlSpecification(
                comparisonNode.getSelector(),
                comparisonNode.getOperator(),
                comparisonNode.getArguments()));
    return result;
  }

  private Specification<ClimbingRoute> createSpecification(
      LogicalNode logicalNode, List<Specification> specs) {
    Specification<ClimbingRoute> result = specs.get(0);
    if (logicalNode.getOperator() == LogicalOperator.AND) {
      for (int i = 1; i < specs.size(); i++) {
        result = Specification.where(result).and(specs.get(i));
      }
    } else if (logicalNode.getOperator() == LogicalOperator.OR) {
      for (int i = 1; i < specs.size(); i++) {
        result = Specification.where(result).or(specs.get(i));
      }
    }
    return result;
  }

}
