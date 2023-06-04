package de.hsrm.mi.team3.swtp.api.dtos;

import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.User;
import java.util.List;

public record GetRoomResponseDTO(
    String roomName, int roomNumber, List<User> userList, String jythonScript) {
  public static GetRoomResponseDTO from(Room r) {
    return new GetRoomResponseDTO(
        r.getRoomName(), r.getRoomNumber(), r.getUserList(), r.getJythonScript());
  }
}
