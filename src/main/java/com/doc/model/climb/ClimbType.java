package com.doc.model.climb;

public enum ClimbType {
  BOULDERING("Bouldering"),
  SPORT("Sport Climbing"),
  AID("Aid Climbing"),
  TRADITIONAL("Trad/Traditional Climbing"),
  FREE_SOLO("Free Soloing"),
  BIG_WALL("Big Wall Climbing"),
  ICE("Ice Climbing"),
  FREE_CLIMBING("Free Climbing");
  private final String displayName;

  ClimbType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
