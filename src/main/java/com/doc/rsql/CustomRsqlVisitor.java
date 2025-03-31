package com.doc.rsql;

import com.doc.model.climb.ClimbingRoute;
import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.springframework.data.jpa.domain.Specification;

public class CustomRsqlVisitor implements RSQLVisitor<Specification<ClimbingRoute>, Void> {

  private ClimbingGradeRsqlSpecBuilder builder;

  public CustomRsqlVisitor() {
    builder = new ClimbingGradeRsqlSpecBuilder();
  }

  @Override
  public Specification<ClimbingRoute> visit(AndNode node, Void param) {
    return builder.createSpecification(node);
  }

  @Override
  public Specification<ClimbingRoute> visit(OrNode node, Void param) {
    return builder.createSpecification(node);
  }

  @Override
  public Specification<ClimbingRoute> visit(ComparisonNode node, Void params) {
    return builder.createSpecification(node);
  }
}
