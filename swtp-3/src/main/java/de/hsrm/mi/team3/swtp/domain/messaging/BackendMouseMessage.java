package de.hsrm.mi.team3.swtp.domain.messaging;

public record BackendMouseMessage(String sessionID, String userName, int roomNumber, int x, int y) {

  /**
   * Parameter for Mouse Message.
   *
   * @param sessionID sessionID
   * @param roomNumber roomNumber of mouse
   * @param x x-coordinate
   * @param y y-coordinate
   * @return BackendMouseMessage
   */
  public static BackendMouseMessage from(
      String sid, String userName, int roomNumber, int x, int y) {
    return new BackendMouseMessage(sid, userName, roomNumber, x, y);
  }
}
