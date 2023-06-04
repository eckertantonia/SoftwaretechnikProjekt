package de.hsrm.mi.team3.swtp.api.dtos;

import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.RoomBox;
import java.util.Map;

public record GetRoomBoxDTO(Map<Integer, Room> rooms) {
  public static GetRoomBoxDTO from(RoomBox r) {
    return new GetRoomBoxDTO(r.getRooms());
  }
}
