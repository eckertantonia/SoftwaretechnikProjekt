package de.hsrm.mi.team3.swtp.api.controllers;

import de.hsrm.mi.team3.swtp.api.dtos.GetRoomResponseDTO;
import de.hsrm.mi.team3.swtp.api.dtos.GetUserResponseDTO;
import de.hsrm.mi.team3.swtp.domain.Room;
import de.hsrm.mi.team3.swtp.domain.User;
import de.hsrm.mi.team3.swtp.domain.messaging.BackendOperation;
import de.hsrm.mi.team3.swtp.domain.messaging.BackendRoomMessage;
import de.hsrm.mi.team3.swtp.services.BackendInfoServiceImpl;
import de.hsrm.mi.team3.swtp.services.RoomBoxServiceImplementation;
import de.hsrm.mi.team3.swtp.services.RoomServiceImplementation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoomRestController {

  Logger logger = LoggerFactory.getLogger(RoomRestController.class);

  @Autowired private RoomBoxServiceImplementation roomBoxService;

  @Autowired private RoomServiceImplementation roomService;

  @Autowired BackendInfoServiceImpl backservice;

  /**
   * Retrieve the Room List saved in the RoomBox Singleton.
   *
   * @return Room List of the RoomBox Singleton.
   */
  @GetMapping(value = "/roomlist", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<GetRoomResponseDTO> getRoomBoxSingleton() {
    List<GetRoomResponseDTO> roomDTOList = new ArrayList<>();
    for (Room room : roomBoxService.getRoomBoxSingelton().getRooms().values()) {
      roomDTOList.add(GetRoomResponseDTO.from(room));
    }
    return roomDTOList;
  }

  /**
   * Retrieve room map of a specific room
   *
   * @param roomNumber
   * @return
   */
  @GetMapping(value = "/room/map/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
  public String getRoomMap(@PathVariable("number") String roomNumber) {
    Room room = roomBoxService.getSpecificRoom(Integer.parseInt(roomNumber));
    if (room.getRoomMap().isEmpty()) {
      return "[]";
    }
    return room.getRoomMap();
  }

  /**
   * Changes the Room a User is in to another.
   *
   * @param roomNumber Room number of room that the User is supposed to be swapped into
   * @param bodyData SessionID and UserName of User that will be moved
   */
  @PostMapping(value = "/room/{number}")
  public void changeRoomOfUser(
      @PathVariable("number") String roomNumber, @RequestBody GetUserResponseDTO bodyData) {
    String sId = bodyData.sessionID();
    Room room = roomBoxService.getSpecificRoom(Integer.parseInt(roomNumber));
    if (roomService.getUserByID(room.getRoomNumber(), sId).isEmpty()) {
      roomService.addNewUserToRoom(
          room,
          new User(sId, Integer.parseInt(roomNumber), bodyData.userName(), new Date().getTime()));
    }
  }

  /**
   * Gets wanted user
   *
   * @param sID Session ID of user
   * @return
   */
  @GetMapping(value = "/room/user/{sessionID}", produces = MediaType.APPLICATION_JSON_VALUE)
  public GetUserResponseDTO getUser(@PathVariable("sessionID") String sID) {
    Optional<User> user = roomBoxService.getUserBySessionID(sID);
    if (user.isPresent()) {
      return GetUserResponseDTO.from(user.get());
    }
    return null;
  }

  /**
   * Removes the user from their current room.
   *
   * @param sessionId SessionID of User that will be moved
   */
  @PostMapping(value = "/user/logout")
  public void logoutUserFromRoom(@RequestBody String sessionId) {
    String sId = sessionId.split(":")[1].replace("\"", "").replace("}", "");
    Optional<User> userOpt = roomBoxService.getUserBySessionID(sId);
    if (userOpt.isPresent()) {
      Room oldRoom = roomBoxService.getRoomsFromRoomBox().get(userOpt.get().getCurrentRoomNumber());
      roomService.removeUserFromRoom(oldRoom, userOpt.get());
      backservice.sendRoom(
          "room/" + oldRoom.getRoomNumber(),
          BackendOperation.UPDATE,
          BackendRoomMessage.from(
              oldRoom.getRoomName(),
              oldRoom.getRoomNumber(),
              oldRoom.getUserList(),
              oldRoom.getJythonScript(),
              oldRoom.getRoomMap()));
    }
  }

  @PostMapping(value = "/reset")
  public void restartEverything() {
    this.roomBoxService.resetEverything();
  }
}
