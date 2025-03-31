package com.doc.model.climb;

public enum ClimbGrade {
  GRADE_1("1"),
  GRADE_2("2"),
  GRADE_3("3"),
  GRADE_4("4"),
  GRADE_4_PLUS("4+"),
  GRADE_5A("5a"),
  GRADE_5A_PLUS("5a+"),
  GRADE_5B("5b"),
  GRADE_5B_PLUS("5b+"),
  GRADE_5C("5c"),
  GRADE_5C_PLUS("5c+"),
  GRADE_6A("6a"),
  GRADE_6A_PLUS("6a+"),
  GRADE_6B("6b"),
  GRADE_6B_PLUS("6b+"),
  GRADE_6C("6c"),
  GRADE_6C_PLUS("6c+");

  private final String grade;

  ClimbGrade(String grade) {
    this.grade = grade;
  }

  public static ClimbGrade fromValue(String value) {
    for (ClimbGrade cg : ClimbGrade.values()) {
      if (cg.getGrade().equalsIgnoreCase(value)) {
        return cg;
      }
    }
    throw new IllegalArgumentException("No ClimbGrade with value " + value);
  }

  public String getGrade() {
    return grade;
  }
}
