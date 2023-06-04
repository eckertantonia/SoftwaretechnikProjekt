package de.hsrm.mi.team3.swtp.domain.messaging;

public enum BackendOperation {
  CREATE("CREATE"),
  UPDATE("UPDATE"),
  DELETE("DELETE");

  private final String type;

  private BackendOperation(String type) {
    this.type = type;
  }

  public String getSize() {
    return type;
  }
}
