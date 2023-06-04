package de.hsrm.mi.team3.swtp.domain.messaging;

import de.hsrm.mi.team3.swtp.domain.User;
import java.util.List;

public record BackendRoomMessage(
    String roomName, int roomNumber, List<User> userList, String jythonScript, String roomMap) {

  /**
   * @param roomName
   * @param roomNumber
   * @param userList
   * @param jythonScript
   * @return BackendRoomMessage
   */
  public static BackendRoomMessage from(
      String roomName, int roomNumber, List<User> userList, String jythonScript, String roomMap) {
    return new BackendRoomMessage(roomName, roomNumber, userList, jythonScript, roomMap);
  }
}
