package com.doc.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CustomRsqlOperators extends RSQLOperators {

  public static final ComparisonOperator IN_CIRCLE =
      new ComparisonOperator(new String[] {"=incircle="},true);

  public static Set<ComparisonOperator> defaultOperators() {
    return new HashSet(
        Arrays.asList(
            EQUAL,
            NOT_EQUAL,
            GREATER_THAN,
            GREATER_THAN_OR_EQUAL,
            LESS_THAN,
            LESS_THAN_OR_EQUAL,
            IN,
            NOT_IN,
            IN_CIRCLE));
  }
}
