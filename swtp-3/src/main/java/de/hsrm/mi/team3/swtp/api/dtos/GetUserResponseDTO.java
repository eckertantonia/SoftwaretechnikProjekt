package de.hsrm.mi.team3.swtp.api.dtos;

import de.hsrm.mi.team3.swtp.domain.User;

public record GetUserResponseDTO(
    String sessionID, int currentRoomNumber, String userName, Long loginTime) {
  public static GetUserResponseDTO from(User u) {
    return new GetUserResponseDTO(
        u.getSessionID(), u.getCurrentRoomNumber(), u.getUserName(), u.getLoginTime());
  }
}
